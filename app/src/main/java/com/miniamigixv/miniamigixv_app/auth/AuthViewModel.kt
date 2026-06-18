package com.miniamigixv.miniamigixv_app.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.miniamigixv.miniamigixv_app.auth.data.repository.AuthRepository
import kotlinx.coroutines.launch

data class AuthState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)

class AuthViewModel : ViewModel() {

    var state by mutableStateOf(AuthState())
        private set

    private val repository = AuthRepository()

    companion object {
        const val WEB_CLIENT_ID = "1048076085575-d8mtgbshn5odilkdv27k34b705vpregg.apps.googleusercontent.com"
    }

    fun updateEmail(email: String) {
        state = state.copy(email = email, emailError = null, errorMessage = null)
    }

    fun updatePassword(password: String) {
        state = state.copy(password = password, passwordError = null, errorMessage = null)
    }

    fun updateConfirmPassword(password: String) {
        state = state.copy(confirmPassword = password, confirmPasswordError = null, errorMessage = null)
    }

    fun login(onSuccess: () -> Unit) {
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)

        if (emailError != null || passwordError != null) {
            state = state.copy(emailError = emailError, passwordError = passwordError)
            return
        }

        state = state.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = repository.login(state.email, state.password)
            result.fold(
                onSuccess = { _ ->
                    state = state.copy(isLoading = false, isLoggedIn = true)
                    onSuccess()
                },
                onFailure = { error ->
                    state = state.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Error al iniciar sesión"
                    )
                }
            )
        }
    }

    fun register(onSuccess: () -> Unit) {
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)
        val confirmError = validateConfirmPassword(state.password, state.confirmPassword)

        if (emailError != null || passwordError != null || confirmError != null) {
            state = state.copy(
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmError
            )
            return
        }

        state = state.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            val result = repository.register(state.email, state.password)
            result.fold(
                onSuccess = { _ ->
                    state = state.copy(isLoading = false, isLoggedIn = true)
                    onSuccess()
                },
                onFailure = { error ->
                    state = state.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Error al registrarse"
                    )
                }
            )
        }
    }

    fun handleGoogleSignInResult(idToken: String, onSuccess: () -> Unit) {
        state = state.copy(isLoading = true, errorMessage = null)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state = state.copy(isLoading = false, isLoggedIn = true)
                    onSuccess()
                } else {
                    state = state.copy(
                        isLoading = false,
                        errorMessage = task.exception?.localizedMessage ?: "Error con Google Sign-In"
                    )
                }
            }
    }

    fun handleGoogleSignInError(errorMessage: String) {
        state = state.copy(errorMessage = errorMessage)
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        state = AuthState()
    }

    private fun validateEmail(email: String): String? {
        if (email.isBlank()) return "El correo es requerido"
        if (!email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")))
            return "Correo inválido"
        return null
    }

    private fun validatePassword(password: String): String? {
        if (password.isBlank()) return "La contraseña es requerida"
        if (password.length < 6) return "Mínimo 6 caracteres"
        return null
    }

    private fun validateConfirmPassword(password: String, confirm: String): String? {
        if (confirm.isBlank()) return "Confirma tu contraseña"
        if (password != confirm) return "Las contraseñas no coinciden"
        return null
    }
}

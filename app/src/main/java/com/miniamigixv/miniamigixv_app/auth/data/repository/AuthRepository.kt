package com.miniamigixv.miniamigixv_app.auth.data.repository

import com.google.gson.Gson
import com.miniamigixv.miniamigixv_app.auth.data.remote.AuthApiClient
import com.miniamigixv.miniamigixv_app.auth.data.remote.AuthErrorResponse
import com.miniamigixv.miniamigixv_app.auth.data.remote.LoginRequest
import com.miniamigixv.miniamigixv_app.auth.data.remote.RegisterRequest

class AuthRepository {

    companion object {
        var useMockApi: Boolean = false
    }

    private val api = AuthApiClient.apiService
    private val gson = Gson()

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            if (useMockApi) {
                mockDelay()
                if (email == "test@test.com" && password == "123456") {
                    Result.success("mock-token-test-user")
                } else {
                    Result.failure(Exception("Credenciales inválidas"))
                }
            } else {
                val response = api.login(LoginRequest(username = email, password = password))
                if (response.isSuccessful) {
                    val body = response.body()
                    Result.success(body?.access ?: "")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = try {
                        gson.fromJson(errorBody, AuthErrorResponse::class.java).error
                    } catch (_: Exception) {
                        "Error de autenticación"
                    }
                    Result.failure(Exception(errorMsg))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.localizedMessage ?: "Sin respuesta del servidor"}"))
        }
    }

    suspend fun register(email: String, password: String): Result<String> {
        return try {
            if (useMockApi) {
                mockDelay()
                if (email.contains("@")) {
                    Result.success("mock-token-new-user")
                } else {
                    Result.failure(Exception("Correo inválido"))
                }
            } else {
                val response = api.register(RegisterRequest(username = email, email = email, password = password))
                if (response.isSuccessful) {
                    // Registration might not return tokens directly depending on Django view
                    // We'll return success and then ViewModel should probably login.
                    // Assuming we modified Django to not return tokens, we just return empty string on success.
                    // But actually our Django view doesn't return tokens, it returns the User object.
                    Result.success("registered")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = try {
                        gson.fromJson(errorBody, AuthErrorResponse::class.java).error
                    } catch (_: Exception) {
                        "Error de registro"
                    }
                    Result.failure(Exception(errorMsg))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.localizedMessage ?: "Sin respuesta del servidor"}"))
        }
    }

    private suspend fun mockDelay() {
        kotlinx.coroutines.delay(1000)
    }
}

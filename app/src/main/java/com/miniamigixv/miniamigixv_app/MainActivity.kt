package com.miniamigixv.miniamigixv_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.miniamigixv.miniamigixv_app.auth.AuthViewModel
import com.miniamigixv.miniamigixv_app.chat.ui.ChatScreen
import com.miniamigixv.miniamigixv_app.screens.HomeScreen
import com.miniamigixv.miniamigixv_app.screens.LoginScreen
import com.miniamigixv.miniamigixv_app.music.ui.MusicScreen
import com.miniamigixv.miniamigixv_app.screens.ProfileScreen
import com.miniamigixv.miniamigixv_app.screens.RegisterScreen
import com.miniamigixv.miniamigixv_app.screens.Screen
import com.miniamigixv.miniamigixv_app.screens.WeatherScreen
import com.miniamigixv.miniamigixv_app.ui.theme.MiniAmigixV_AppTheme

class MainActivity : ComponentActivity() {
    private val authViewModel = AuthViewModel()
    private var onGoogleSignInSuccess: (() -> Unit)? = null

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                authViewModel.handleGoogleSignInResult(idToken) {
                    onGoogleSignInSuccess?.invoke()
                }
            }
        } catch (e: ApiException) {
            authViewModel.handleGoogleSignInError("Error con Google Sign-In: ${e.message}")
        }
    }

    fun signInWithGoogle(onSuccess: () -> Unit) {
        onGoogleSignInSuccess = onSuccess
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(AuthViewModel.WEB_CLIENT_ID)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MiniAmigixV_AppTheme {
                AppNavigation(this)
            }
        }
    }
}

@Composable
fun AppNavigation(mainActivity: MainActivity) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToHome = { navController.navigate(Screen.Home.route) },
                onGoogleSignIn = { mainActivity.signInWithGoogle { navController.navigate(Screen.Home.route) } }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToHome = { navController.navigate(Screen.Home.route) },
                onGoogleSignIn = { mainActivity.signInWithGoogle { navController.navigate(Screen.Home.route) } }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToWeather = { navController.navigate(Screen.Weather.route) },
                onNavigateToMusic = { navController.navigate(Screen.Music.route) },
                onNavigateToChat = { navController.navigate(Screen.Chat.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToGames = { /* TODO: Implement Games screen */ },
                onNavigateToStudy = { /* TODO: Implement Study screen */ },
                onNavigateToEvents = { /* TODO: Implement Events screen */ },
                onNavigateToTranslator = { /* TODO: Implement Translator screen */ },
                onNavigateToEntertainment = { /* TODO: Implement Entertainment screen */ },
                onNavigateToBlog = { /* TODO: Implement Blog screen */ },
                onNavigateToSupport = { /* TODO: Implement Support screen */ },
                onNavigateToAdminCenter = { /* TODO: Implement Admin Center screen */ },
                onLogout = { navController.popBackStack(Screen.Login.route, inclusive = true) }
            )
        }
        composable(Screen.Weather.route) {
            WeatherScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Music.route) {
            MusicScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Chat.route) {
            ChatScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Profile.route) {
            ProfileScreen(onBack = { navController.popBackStack() })
        }
    }
}
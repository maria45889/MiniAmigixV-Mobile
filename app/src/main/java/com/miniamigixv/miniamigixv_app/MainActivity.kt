package com.miniamigixv.miniamigixv_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.miniamigixv.miniamigixv_app.screens.EditProfileScreen
import com.miniamigixv.miniamigixv_app.screens.RegisterScreen
import com.miniamigixv.miniamigixv_app.screens.Screen
import com.miniamigixv.miniamigixv_app.screens.WeatherScreen
import com.miniamigixv.miniamigixv_app.screens.GamesScreen
import com.miniamigixv.miniamigixv_app.games.MemoriaNeonScreen
import com.miniamigixv.miniamigixv_app.games.RespiracionConscienteScreen
import com.miniamigixv.miniamigixv_app.games.SnakeNeoScreen
import com.miniamigixv.miniamigixv_app.games.TicTacToeScreen
import com.miniamigixv.miniamigixv_app.screens.StudyScreen
import com.miniamigixv.miniamigixv_app.screens.EventsScreen
import com.miniamigixv.miniamigixv_app.screens.TranslatorScreen
import com.miniamigixv.miniamigixv_app.screens.EntertainmentScreen
import com.miniamigixv.miniamigixv_app.screens.BlogScreen
import com.miniamigixv.miniamigixv_app.screens.SupportScreen
import com.miniamigixv.miniamigixv_app.screens.AdminCenterScreen
import com.miniamigixv.miniamigixv_app.screens.TutorialScreen
import com.miniamigixv.miniamigixv_app.screens.SettingsScreen
import com.miniamigixv.miniamigixv_app.screens.NotificationsScreen
import com.miniamigixv.miniamigixv_app.ui.theme.MiniAmigixV_AppTheme
import com.miniamigixv.miniamigixv_app.ui.theme.ThemeViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel = AuthViewModel()
    private lateinit var themeViewModel: ThemeViewModel
    private var onGoogleSignInSuccess: (() -> Unit)? = null

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        Log.d("GoogleSignIn", "Google Sign-In result received")
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            Log.d("GoogleSignIn", "Account obtained: ${account.email}")
            val idToken = account.idToken
            Log.d("GoogleSignIn", "ID Token: ${if (idToken != null) "Present" else "NULL"}")
            if (idToken != null) {
                authViewModel.handleGoogleSignInResult(idToken) {
                    onGoogleSignInSuccess?.invoke()
                }
            } else {
                Log.e("GoogleSignIn", "ID Token is null")
                authViewModel.handleGoogleSignInError("Error: ID Token es nulo")
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Google Sign-In failed", e)
            authViewModel.handleGoogleSignInError("Error con Google Sign-In: ${e.message} (Código: ${e.statusCode})")
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
        themeViewModel = ThemeViewModel(this)

        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            MiniAmigixV_AppTheme(darkTheme = isDarkTheme) {
                AppNavigation(this, themeViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(mainActivity: MainActivity, themeViewModel: ThemeViewModel) {
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
                onNavigateToHome = { navController.navigate(Screen.Home.route) }
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
                themeViewModel = themeViewModel,
                onNavigateToWeather = { navController.navigate(Screen.Weather.route) },
                onNavigateToMusic = { navController.navigate(Screen.Music.route) },
                onNavigateToChat = { navController.navigate(Screen.Chat.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToGames = { navController.navigate(Screen.Games.route) },
                onNavigateToStudy = { navController.navigate(Screen.Study.route) },
                onNavigateToEvents = { navController.navigate(Screen.Events.route) },
                onNavigateToTranslator = { navController.navigate(Screen.Translator.route) },
                onNavigateToEntertainment = { navController.navigate(Screen.Entertainment.route) },
                onNavigateToBlog = { navController.navigate(Screen.Blog.route) },
                onNavigateToSupport = { navController.navigate(Screen.Support.route) },
                onNavigateToAdminCenter = { navController.navigate(Screen.AdminCenter.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToTutorial = { navController.navigate(Screen.Tutorial.route) },
                onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                onLogout = { navController.popBackStack(Screen.Login.route, inclusive = true) }
            )
        }
        composable(Screen.Weather.route) {
            WeatherScreen(themeViewModel = themeViewModel, onBack = { navController.popBackStack() })
        }
        composable(Screen.Music.route) {
            MusicScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Chat.route) {
            ChatScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate(Screen.EditProfile.route) }
            )
        }
        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                onBack = { navController.popBackStack() },
                onSave = { username, bio, birthdate, theme, language ->
                    // Update theme based on selection
                    themeViewModel.setTheme(theme == "Oscuro")
                    // TODO: Save profile data to backend/state
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Games.route) {
            GamesScreen(
                onBack = { navController.popBackStack() },
                onNavigateToMemoriaNeon = { navController.navigate(Screen.MemoriaNeon.route) },
                onNavigateToRespiracionConsciente = { navController.navigate(Screen.RespiracionConsciente.route) },
                onNavigateToSnakeNeo = { navController.navigate(Screen.SnakeNeo.route) },
                onNavigateToTicTacToe = { navController.navigate(Screen.TicTacToe.route) }
            )
        }
        composable(Screen.MemoriaNeon.route) {
            MemoriaNeonScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.RespiracionConsciente.route) {
            RespiracionConscienteScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.SnakeNeo.route) {
            SnakeNeoScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.TicTacToe.route) {
            TicTacToeScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Study.route) {
            StudyScreen(themeViewModel = themeViewModel, onBack = { navController.popBackStack() })
        }
        composable(Screen.Events.route) {
            EventsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Translator.route) {
            TranslatorScreen(themeViewModel = themeViewModel, onBack = { navController.popBackStack() })
        }
        composable(Screen.Entertainment.route) {
            EntertainmentScreen(themeViewModel = themeViewModel, onBack = { navController.popBackStack() })
        }
        composable(Screen.Blog.route) {
            BlogScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Support.route) {
            SupportScreen(themeViewModel = themeViewModel, onBack = { navController.popBackStack() })
        }
        composable(Screen.AdminCenter.route) {
            AdminCenterScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Tutorial.route) {
            TutorialScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() }, themeViewModel = themeViewModel)
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen(themeViewModel = themeViewModel, onBack = { navController.popBackStack() })
        }
    }
}
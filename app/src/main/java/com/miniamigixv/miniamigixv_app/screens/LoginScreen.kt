package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miniamigixv.miniamigixv_app.R
import com.miniamigixv.miniamigixv_app.auth.AuthViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val state = authViewModel.state
    val focusManager = LocalFocusManager.current
    
    // Animations
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF24243E))
                )
            )
            .systemBarsPadding()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Logo with glow effect
        Box(
            modifier = Modifier
                .scale(scale)
                .size(100.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFa855f7).copy(alpha = glowAlpha),
                            Color.Transparent
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo MiniAmigixV",
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "MiniAmigixV",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFa855f7)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Bienvenido de nuevo",
            fontSize = 18.sp,
            color = Color(0xFFe0e0e0)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFa855f7).copy(alpha = 0.5f),
                            Color(0xFF3b82f6).copy(alpha = 0.5f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = authViewModel::updateEmail,
                    label = { Text("Correo electrónico", color = Color(0xFFa855f7)) },
                    isError = state.emailError != null,
                    supportingText = state.emailError?.let { { Text(it, color = Color.Red) } },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFa855f7),
                        unfocusedBorderColor = Color(0xFF4a4a6a),
                        focusedLabelColor = Color(0xFFa855f7),
                        unfocusedLabelColor = Color(0xFF8888a8)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = authViewModel::updatePassword,
                    label = { Text("Contraseña", color = Color(0xFFa855f7)) },
                    isError = state.passwordError != null,
                    supportingText = state.passwordError?.let { { Text(it, color = Color.Red) } },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            authViewModel.loginWithFirebase(onNavigateToHome)
                        }
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFa855f7),
                        unfocusedBorderColor = Color(0xFF4a4a6a),
                        focusedLabelColor = Color(0xFFa855f7),
                        unfocusedLabelColor = Color(0xFF8888a8)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = Color(0xFFa855f7),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { }
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (state.errorMessage != null) {
                    Text(
                        text = state.errorMessage,
                        color = Color.Red,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        authViewModel.loginWithFirebase(onNavigateToHome)
                    },
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFFa855f7), Color(0xFF3b82f6))
                            ),
                            shape = RoundedCornerShape(25.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("Entrar", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿No tienes cuenta? ", fontSize = 14.sp, color = Color(0xFFe0e0e0))
            Text(
                text = "Regístrate aquí",
                color = Color(0xFFa855f7),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.clickable(enabled = !state.isLoading) { onNavigateToRegister() }
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

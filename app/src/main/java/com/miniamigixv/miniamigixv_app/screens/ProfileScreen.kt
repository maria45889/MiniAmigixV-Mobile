package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miniamigixv.miniamigixv_app.auth.AuthViewModel
import com.miniamigixv.miniamigixv_app.ui.components.*
import com.miniamigixv.miniamigixv_app.ui.theme.ThemeViewModel

private val NeonPurple = Color(0xFF8B5CF6)
private val NeonCyan = Color(0xFF22D3EE)
private val NeonGreen = Color(0xFF10B981)
private val NeonOrange = Color(0xFFF59E0B)
private val NeonPink = Color(0xFFF472B6)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit = {},
    onNavigateToEdit: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel(),
    themeViewModel: ThemeViewModel = viewModel()
) {
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).systemBarsPadding()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Perfil", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Profile Header Card
                item {
                    NeonBorderCard(padding = 24.dp) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .border(width = 3.dp, brush = Brush.linearGradient(listOf(NeonPurple, NeonCyan)), shape = CircleShape)
                                    .background(NeonPurple.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Person, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(50.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("María José", color = MaterialTheme.colorScheme.onBackground, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                if (false) "\"Tu biografía aquí\"" else "Sin biografía",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp,
                                fontStyle = if (false) androidx.compose.ui.text.font.FontStyle.Italic else androidx.compose.ui.text.font.FontStyle.Normal
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                NeonChip(text = "📅 Miembro desde Jun 2026", icon = Icons.Filled.CalendarMonth)
                                NeonChip(text = if (isDarkTheme) "🌙 Oscuro" else "☀️ Claro", icon = Icons.Filled.DarkMode)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onNavigateToEdit,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                contentPadding = PaddingValues(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            brush = Brush.horizontalGradient(listOf(NeonPurple, NeonCyan)),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 24.dp, vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Editar Perfil", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // Level Progress
                item {
                    NeonBorderCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Nivel", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Brush.horizontalGradient(listOf(NeonPurple, NeonPink)))
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text("Nivel 1", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("0 / 100 XP", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(Brush.horizontalGradient(listOf(NeonPurple, NeonCyan)))
                            )
                        }
                    }
                }

                // Stats
                item {
                    NeonBorderCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.BarChart, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Estadísticas", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                StatItem("📅", "0", "Eventos", Modifier.weight(1f))
                                StatItem("🔔", "0", "Alertas", Modifier.weight(1f))
                                StatItem("🔥", "0", "Días", Modifier.weight(1f))
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                StatItem("💬", "0", "Chats IA", Modifier.weight(1f))
                                StatItem("🎵", "0", "Canciones", Modifier.weight(1f))
                                StatItem("🎮", "0", "Juegos", Modifier.weight(1f))
                            }
                        }
                    }
                }

                // Achievements
                item {
                    NeonBorderCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Star, contentDescription = null, tint = NeonOrange, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Logros", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AchievementItem("🏆", "Primer Chat", "Envía tu primer mensaje", Modifier.weight(1f))
                            AchievementItem("🎵", "Melómano", "Escucha 10 canciones", Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AchievementItem("📚", "Estudiante", "Completa 5 estudios", Modifier.weight(1f), locked = true)
                            AchievementItem("🎮", "Jugador", "Juega 10 partidas", Modifier.weight(1f), locked = true)
                        }
                    }
                }

                // Preferences
                item {
                    NeonBorderCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Settings, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Preferencias", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            PreferenceItem("🌙", "Tema", if (isDarkTheme) "Oscuro" else "Claro")
                            PreferenceItem("🌎", "Idioma", "ES")
                            PreferenceItem("🎨", "Color", "Púrpura")
                            PreferenceItem("⏰", "Reloj", "24 horas")
                        }
                    }
                }

                // Account Info
                item {
                    NeonBorderCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Person, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cuenta", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            AccountInfoItem("📧", "Correo", "maria@email.com")
                            AccountInfoItem("📅", "Miembro desde", "Jun 2026")
                        }
                    }
                }

                // Security
                item {
                    NeonBorderCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Shield, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Seguridad", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        SecurityItem("🔒", "Cambiar contraseña")
                        Spacer(modifier = Modifier.height(8.dp))
                        SecurityItem("📱", "Sesiones activas: 1")
                    }
                }

                // Logout
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            authViewModel.logout()
                            onBack()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                        border = BorderStroke(1.dp, Color(0xFFEF4444))
                    ) {
                        Icon(Icons.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar sesión", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun StatItem(icon: String, value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(12.dp)
    ) {
        Text(text = icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
    }
}

@Composable
private fun AchievementItem(
    icon: String,
    name: String,
    description: String,
    modifier: Modifier = Modifier,
    locked: Boolean = false
) {
    val alpha = if (locked) 0.5f else 1f
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            .padding(12.dp)
    ) {
        Text(text = if (locked) "🔒" else icon, fontSize = 24.sp, modifier = Modifier.alpha(alpha))
        Spacer(modifier = Modifier.height(8.dp))
        Text(name, color = MaterialTheme.colorScheme.onBackground, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.alpha(alpha))
        Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, modifier = Modifier.alpha(alpha))
    }
}

@Composable
private fun PreferenceItem(icon: String, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            .padding(12.dp)
    ) {
        Text(text = icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            Text(value, color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun AccountInfoItem(icon: String, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            .padding(12.dp)
    ) {
        Text(text = icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label.uppercase(), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, letterSpacing = 1.sp)
            Text(value, color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SecurityItem(icon: String, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
            .padding(12.dp)
    ) {
        Text(text = icon, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
    }
}

package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.GlassCard
import com.miniamigixv.miniamigixv_app.ui.theme.ThemeViewModel

private val NeonPurple = Color(0xFF8B5CF6)
private val NeonBlue = Color(0xFF06B6D4)
private val NeonCyan = Color(0xFF22D3EE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeViewModel: ThemeViewModel,
    onBack: () -> Unit = {}
) {
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF09090F), Color(0xFF0F172A))
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        "Configuracion",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    ProfileSection()
                }

                item {
                    SectionHeader("Apariencia")
                }
                item {
                    GlassCard {
                        SettingToggleRow(
                            icon = Icons.Filled.DarkMode,
                            label = "Modo oscuro",
                            subtitle = "Activa el tema oscuro",
                            checked = isDarkTheme,
                            onCheckedChange = { themeViewModel.setTheme(it) }
                        )
                        GlassDivider()
                        SettingClickRow(
                            icon = Icons.Filled.Palette,
                            label = "Color de acento",
                            subtitle = "Personaliza el color primario",
                            value = "Purpura"
                        )
                    }
                }

                item {
                    SectionHeader("Notificaciones")
                }
                item {
                    GlassCard {
                        SettingToggleRow(
                            icon = Icons.Filled.Notifications,
                            label = "Push",
                            subtitle = "Notificaciones push",
                            checked = true,
                            onCheckedChange = {}
                        )
                        GlassDivider()
                        SettingToggleRow(
                            icon = Icons.Filled.Email,
                            label = "Email",
                            subtitle = "Notificaciones por correo",
                            checked = false,
                            onCheckedChange = {}
                        )
                        GlassDivider()
                        SettingToggleRow(
                            icon = Icons.Filled.VolumeUp,
                            label = "Sonido",
                            subtitle = "Sonido de notificaciones",
                            checked = true,
                            onCheckedChange = {}
                        )
                    }
                }

                item {
                    SectionHeader("Cuenta")
                }
                item {
                    GlassCard {
                        SettingClickRow(
                            icon = Icons.Filled.Lock,
                            label = "Cambiar contrasena",
                            subtitle = "Actualiza tu contrasena",
                            value = ""
                        )
                        GlassDivider()
                        SettingClickRow(
                            icon = Icons.Filled.DeleteForever,
                            label = "Eliminar cuenta",
                            subtitle = "Borra permanentemente tu cuenta",
                            value = "",
                            valueColor = Color(0xFFEF4444)
                        )
                    }
                }

                item {
                    SectionHeader("Acerca de")
                }
                item {
                    GlassCard {
                        SettingClickRow(
                            icon = Icons.Filled.Info,
                            label = "Version de la app",
                            subtitle = "Version actual",
                            value = "1.0.0"
                        )
                        GlassDivider()
                        SettingClickRow(
                            icon = Icons.Filled.Build,
                            label = "Build number",
                            subtitle = "Numero de compilacion",
                            value = "2024.07.06"
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun ProfileSection() {
    GlassCard(cornerRadius = 24.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(NeonPurple.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = null,
                    tint = NeonPurple,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "Usuario",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    "usuario@email.com",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        color = NeonPurple,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun SettingToggleRow(
    icon: ImageVector,
    label: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = NeonBlue,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(subtitle, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NeonPurple,
                uncheckedThumbColor = Color.White.copy(alpha = 0.4f),
                uncheckedTrackColor = Color.White.copy(alpha = 0.15f)
            )
        )
    }
}

@Composable
private fun SettingClickRow(
    icon: ImageVector,
    label: String,
    subtitle: String,
    value: String,
    valueColor: Color = Color.White.copy(alpha = 0.6f)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = NeonBlue,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(subtitle, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        }
        if (value.isNotEmpty()) {
            Text(
                value,
                color = valueColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Icon(
            Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.3f),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun GlassDivider() {
    Spacer(modifier = Modifier.height(8.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.05f))
    )
    Spacer(modifier = Modifier.height(8.dp))
}

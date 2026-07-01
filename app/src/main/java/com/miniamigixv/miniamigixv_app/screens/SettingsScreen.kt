package com.miniamigixv.miniamigixv_app.screens

import android.content.Context
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.*
import com.miniamigixv.miniamigixv_app.ui.theme.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit = {}, themeViewModel: ThemeViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("General", "Privacidad", "Accesibilidad", "Apariencia", "Seguridad")
    val tabIcons = listOf(Icons.Filled.Settings, Icons.Filled.Lock, Icons.Filled.Accessibility, Icons.Filled.Palette, Icons.Filled.Shield)
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        TopAppBar(
            title = { Text("Configuración", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        NeonHeader(title = "CONFIGURACIÓN", subtitle = "Personaliza tu experiencia y ajusta tus preferencias.")
        Spacer(modifier = Modifier.height(16.dp))

        // ScrollableTabRow
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = NeonCyan,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = NeonCyan
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(tabIcons[index], contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                title,
                                color = if (selectedTab == index) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTab) {
                0 -> { // General
                    item { SettingsSection("Apariencia", "Personaliza el aspecto visual de la interfaz", Icons.Filled.Palette, NeonPurple) }
                    item { SettingsToggleRow("Tema de la Interfaz", "Elige entre modo claro y oscuro", isDarkTheme, onCheckedChange = { themeViewModel.setTheme(it) }) }
                    item { SettingsClickRow("Color de Acento", "Elige tu color primario de la interfaz", "●●●●●") }
                    item { SettingsSection("Idiomas", "Configure el idioma de la aplicación", Icons.Filled.Language, NeonCyan) }
                    item { SettingsClickRow("Idioma preferido", "Establece tu idioma de la aplicación", "Español") }
                    item { SettingsClickRow("Formato de Fecha", "Define tu formato de formato de tiempo", "DD/MM/AAAA") }
                    item { SettingsClickRow("Formato de reloj", "Define tu formato del reloj", "24 horas") }
                    item { SettingsToggleRow("Mostrar segundos", "Muestra los segundos en el reloj", false) }
                    item { SettingsToggleRow("Mostrar Radio", "Muestra la radio en el reloj", true) }
                    item { SettingsClickRow("Zona horaria", "Define tu zona horaria actual", "UTC-5") }
                }
                1 -> { // Privacidad
                    item { SettingsSection("Datos", "Controla tu información personal", Icons.Filled.DataUsage, NeonPurple) }
                    item { SettingsToggleRow("Compartir datos de uso", "Ayúdanos a mejorar compartiendo estadísticas anónimas", false) }
                    item { SettingsClickRow("Descargar mis datos", "Exporta toda tu información personal", "") }
                    item { SettingsSection("Historial", "Administra tu historial de actividad", Icons.Filled.History, NeonCyan) }
                    item { SettingsClickRow("Borrar historial de chat", "Elimina todas las conversaciones anteriores", "") }
                    item { SettingsClickRow("Borrar historial de búsqueda", "Elimina todas las búsquedas recientes", "") }
                }
                2 -> { // Accesibilidad
                    item { SettingsSection("Visual", "Ajusta la experiencia visual", Icons.Filled.Visibility, NeonPurple) }
                    item { SettingsToggleRow("Texto grande", "Aumenta el tamaño del texto en toda la app", false) }
                    item { SettingsToggleRow("Alto contraste", "Mejora el contraste de colores", false) }
                    item { SettingsToggleRow("Reducir animaciones", "Minimiza las animaciones y transiciones", false) }
                }
                3 -> { // Apariencia
                    item { SettingsSection("Tema", "Personaliza tu interfaz", Icons.Filled.ColorLens, NeonPurple) }
                    item { SettingsToggleRow("Modo oscuro", "Activa el tema oscuro para reducir brillo", isDarkTheme, onCheckedChange = { themeViewModel.setTheme(it) }) }
                    item { SettingsToggleRow("Efectos neón", "Habilita los efectos de brillo neón", true) }
                    item { SettingsClickRow("Fondo personalizado", "Elige un fondo de pantalla personalizado", "") }
                    item { SettingsSection("Brillo", "Controla el brillo de tu dispositivo", Icons.Filled.Brightness6, NeonCyan) }
                    item { BrightnessSlider(context = context) }
                }
                4 -> { // Seguridad
                    item { SettingsSection("Sesiones Activas", "Dispositivos donde tienes la sesión iniciada.", Icons.Filled.Devices, NeonPurple) }
                    item {
                        NeonCard {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.PhoneAndroid, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Este dispositivo actual", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("Google Chrome en Windows", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                                    Text("Activo", color = Color(0xFF10B981), fontSize = 11.sp)
                                }
                            }
                        }
                    }
                    item { SettingsSection("Cuenta", "Administra tu sesión", Icons.Filled.AccountCircle, Color(0xFFEF4444)) }
                    item { SettingsClickRow("Cambiar contraseña", "Actualiza tu contraseña de acceso", "") }
                    item { SettingsClickRow("Cerrar todas las sesiones", "Cierra sesión en todos los dispositivos", "", valueColor = Color(0xFFEF4444)) }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                NeonButton(
                    text = "Guardar Cambios",
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth(),
                    icon = { Icon(Icons.Filled.Save, contentDescription = null, tint = MaterialTheme.colorScheme.background, modifier = Modifier.size(18.dp)) }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun SettingsSection(title: String, subtitle: String, icon: ImageVector, iconColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
    }
}

@Composable
private fun SettingsToggleRow(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit = {}) {
    NeonCard(padding = 12.dp) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.background,
                    checkedTrackColor = NeonCyan,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}

@Composable
private fun SettingsClickRow(title: String, subtitle: String, value: String, valueColor: Color = MaterialTheme.colorScheme.onBackground) {
    NeonCard(padding = 12.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            }
            if (value.isNotEmpty()) {
                Text(value, color = valueColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(4.dp))
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun BrightnessSlider(context: Context) {
    var brightness by remember { mutableStateOf(getSystemBrightness(context)) }

    NeonCard(padding = 16.dp) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Brillo de pantalla",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
                Text(
                    "${(brightness * 100).toInt()}%",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Slider(
                value = brightness,
                onValueChange = {
                    brightness = it
                },
                onValueChangeFinished = {
                    setSystemBrightness(context, brightness)
                },
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    activeTrackColor = NeonCyan,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                    thumbColor = MaterialTheme.colorScheme.background
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Filled.Brightness5,
                    contentDescription = "Brillo bajo",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Icon(
                    Icons.Filled.Brightness7,
                    contentDescription = "Brillo alto",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun getSystemBrightness(context: Context): Float {
    return try {
        Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS) / 255f
    } catch (e: Settings.SettingNotFoundException) {
        0.5f
    }
}

private fun setSystemBrightness(context: Context, brightness: Float) {
    try {
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS,
            (brightness * 255).toInt()
        )
    } catch (e: Exception) {
        // Handle permission or other errors
    }
}

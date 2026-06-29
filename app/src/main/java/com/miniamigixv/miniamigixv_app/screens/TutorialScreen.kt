package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.*

private data class TutorialModule(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialScreen(onBack: () -> Unit = {}) {
    val mainModules = listOf(
        TutorialModule("Inicio", "Descubre el dashboard, accesos rápidos, y gestiona tus actividades.", Icons.Filled.Home, Color(0xFFEF4444)),
        TutorialModule("Chat IA", "Conversa con nuestro asistente inteligente y pregúntale lo que necesites.", Icons.Filled.SmartToy, NeonCyan),
        TutorialModule("Música", "Administra tus listas de reproducción y descubre nueva música.", Icons.Filled.LibraryMusic, NeonPurple),
        TutorialModule("Juegos", "Diviértete con nuestra colección de juegos integrados.", Icons.Filled.SportsEsports, Color(0xFF10B981)),
        TutorialModule("Eventos", "Crea eventos, asócialo a calendarios y recibe notificaciones.", Icons.Filled.Event, Color(0xFFF59E0B)),
        TutorialModule("Estudio", "Herramientas de estudio y productividad para ser más eficiente.", Icons.Filled.School, Color(0xFFEC4899)),
        TutorialModule("Notificaciones", "No te pierdas ninguna alerta. Aprende a personalizar tus notificaciones.", Icons.Filled.Notifications, NeonCyan),
        TutorialModule("Perfil", "Edita tu avatar, cambia tu nombre y ajusta tu avatar a tu gusto.", Icons.Filled.Person, Color(0xFF10B981))
    )

    val configModules = listOf(
        TutorialModule("Tema Oscuro/Claro", "Cambia la apariencia de tu app. Oscuro por defecto.", Icons.Filled.DarkMode, NeonPurple),
        TutorialModule("Idioma y Preferencia", "Selecciona tu idioma preferido para que MiniAmigixV te hable como quieras.", Icons.Filled.Language, NeonCyan)
    )

    Column(modifier = Modifier.fillMaxSize().background(BgDark)) {
        TopAppBar(
            title = { Text("Tutorial", fontWeight = FontWeight.Bold, color = TextWhite) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = TextWhite)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBg)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 170.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            item(span = { GridItemSpan(maxLineSpan) }) {
                NeonBorderCard(padding = 24.dp) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Filled.School, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Tutorial MiniAmigixV", color = NeonCyan, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Bienvenido 👋 aquí aprenderás a usar la app de forma interactiva.", color = TextGray, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        NeonButton(
                            text = "Iniciar Guía Interactiva",
                            onClick = { /* TODO */ },
                            modifier = Modifier.fillMaxWidth(0.7f),
                            icon = { Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = BgDark) }
                        )
                    }
                }
            }

            // Section: Módulos principales
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("📚 Módulos principales", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            items(mainModules) { module ->
                TutorialModuleCard(module)
            }

            // Section: Configuración
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("⚙ Configuración", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            items(configModules) { module ->
                TutorialModuleCard(module)
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun TutorialModuleCard(module: TutorialModule) {
    NeonCard(padding = 16.dp) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(module.iconColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(module.icon, contentDescription = null, tint = module.iconColor, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(module.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(module.description, color = TextGray, fontSize = 11.sp, lineHeight = 16.sp)
    }
}

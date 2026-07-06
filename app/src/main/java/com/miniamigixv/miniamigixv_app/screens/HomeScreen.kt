package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.miniamigixv.miniamigixv_app.R
import com.miniamigixv.miniamigixv_app.ui.components.GlassCard
import com.miniamigixv.miniamigixv_app.ui.components.GlassPanel
import com.miniamigixv.miniamigixv_app.ui.components.NeonGlowBox
import com.miniamigixv.miniamigixv_app.ui.theme.ThemeViewModel

private val NeonPurple = Color(0xFF8B5CF6)
private val NeonBlue = Color(0xFF06B6D4)
private val NeonCyan = Color(0xFF22D3EE)
private val NeonPink = Color(0xFFF472B6)
private val NeonGreen = Color(0xFF10B981)
private val NeonOrange = Color(0xFFF59E0B)
private val ErrorRed = Color(0xFFEF4444)

private data class ModuleCard(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconColor: Color,
    val onClick: () -> Unit
)

private data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val color: Color = Color.Unspecified
)

private data class QuickAccessItem(
    val title: String,
    val iconText: String,
    val gradient: List<Color>,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String = "María José",
    themeViewModel: ThemeViewModel? = null,
    onNavigateToWeather: () -> Unit = {},
    onNavigateToMusic: () -> Unit = {},
    onNavigateToChat: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToGames: () -> Unit = {},
    onNavigateToStudy: () -> Unit = {},
    onNavigateToEvents: () -> Unit = {},
    onNavigateToTranslator: () -> Unit = {},
    onNavigateToEntertainment: () -> Unit = {},
    onNavigateToBlog: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateToAdminCenter: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToTutorial: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var showSuggestionsDialog by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val isDarkTheme by themeViewModel?.isDarkTheme?.collectAsState() ?: remember { mutableStateOf(true) }

    val modules = listOf(
        ModuleCard("Chat IA", "Conversa con tu asistente virtual inteligente.", Icons.Filled.SmartToy, NeonPink, onNavigateToChat),
        ModuleCard("Música", "Escucha tus canciones favoritas.", Icons.Filled.LibraryMusic, NeonBlue, onNavigateToMusic),
        ModuleCard("Juegos", "Diviértete con nuestra colección.", Icons.Filled.SportsEsports, NeonGreen, onNavigateToGames),
        ModuleCard("Estudio", "Herramientas para mejorar tu aprendizaje.", Icons.Filled.School, NeonOrange, onNavigateToStudy),
        ModuleCard("Clima", "Revisa el pronóstico actual.", Icons.Filled.Cloud, NeonCyan, onNavigateToWeather),
        ModuleCard("Blog", "Lee y comparte tus ideas.", Icons.Filled.Article, NeonPurple, onNavigateToBlog)
    )

    val menuItems = listOf(
        MenuItem("Inicio", Icons.Filled.Home, {}),
        MenuItem("Chat IA", Icons.Filled.SmartToy, onNavigateToChat),
        MenuItem("Música", Icons.Filled.LibraryMusic, onNavigateToMusic),
        MenuItem("Juegos", Icons.Filled.SportsEsports, onNavigateToGames),
        MenuItem("Estudio", Icons.Filled.School, onNavigateToStudy),
        MenuItem("Eventos", Icons.Filled.Event, onNavigateToEvents),
        MenuItem("Clima", Icons.Filled.Cloud, onNavigateToWeather),
        MenuItem("Traductor", Icons.Filled.Translate, onNavigateToTranslator),
        MenuItem("Entretenimiento", Icons.Filled.AutoAwesome, onNavigateToEntertainment),
        MenuItem("Blog", Icons.Filled.Article, onNavigateToBlog),
        MenuItem("Soporte", Icons.Filled.SupportAgent, onNavigateToSupport),
        MenuItem("Tutorial", Icons.Filled.Info, onNavigateToTutorial)
    )

    val recentActivities = listOf(
        "Chat IA" to "Hoy",
        "Música" to "Hoy",
        "Evento" to "Hoy",
        "Traductor" to "Hoy"
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        menuItems.forEachIndexed { index, item ->
                            val isSelected = index == 0
                            NavigationDrawerItem(
                                icon = { Icon(item.icon, contentDescription = item.title, tint = item.color) },
                                label = { Text(item.title, color = item.color, fontSize = 14.sp) },
                                selected = isSelected,
                                onClick = {
                                    item.onClick()
                                    coroutineScope.launch { drawerState.close() }
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent,
                                    selectedContainerColor = Color.Transparent
                                ),
                                modifier = Modifier
                                    .padding(vertical = 2.dp)
                                    .then(
                                        if (isSelected) Modifier.background(
                                            Brush.horizontalGradient(
                                                colors = listOf(NeonBlue.copy(alpha = 0.3f), Color.Transparent)
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        ) else Modifier
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        
                        NavigationDrawerItem(
                            icon = { Icon(Icons.Filled.Logout, contentDescription = "Cerrar Sesión", tint = ErrorRed) },
                            label = { Text("Cerrar Sesión", color = ErrorRed, fontSize = 14.sp) },
                            selected = false,
                            onClick = {
                                onLogout()
                                coroutineScope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Brush.horizontalGradient(listOf(NeonPurple, NeonBlue, NeonCyan)))
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    
                    Divider(color = MaterialTheme.colorScheme.surface)
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onNavigateToProfile) {
                            Icon(Icons.Filled.PersonOutline, contentDescription = "Perfil", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = { showSuggestionsDialog = true }) {
                            Icon(Icons.Filled.HelpOutline, contentDescription = "Ayuda / Sugerencias", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(Icons.Filled.Settings, contentDescription = "Configuración", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("MiniAmigixV", color = NeonPurple, fontWeight = FontWeight.Bold)
                            Text(" / Inicio", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 16.sp)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToNotifications) {
                            Icon(Icons.Filled.NotificationsNone, contentDescription = "Notificaciones", tint = MaterialTheme.colorScheme.onBackground)
                        }
                        IconButton(onClick = { themeViewModel?.toggleTheme() }) {
                            Icon(
                                if (isDarkTheme) Icons.Filled.DarkMode else Icons.Filled.LightMode, 
                                contentDescription = "Cambiar Tema", 
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
                    .systemBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hero Section
                GlassPanel(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 28.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Text(
                            text = "Bienvenido a MiniAmigixV",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tu espacio personal interactivo",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "🤖",
                            fontSize = 64.sp
                        )
                    }
                }

                // Frase del Día and Actividad Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FraseDelDiaCard(Modifier.weight(1f))
                    TuActividadCard(Modifier.weight(1f))
                }

                // Quick Access Section
                Text(
                    text = "Accesos Rápidos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickAccessItem(
                        title = "IA",
                        iconText = "💬",
                        gradient = listOf(NeonPink, NeonOrange),
                        onClick = onNavigateToChat,
                        modifier = Modifier.weight(1f)
                    )
                    QuickAccessItem(
                        title = "Música",
                        iconText = "🎵",
                        gradient = listOf(NeonBlue, NeonCyan),
                        onClick = onNavigateToMusic,
                        modifier = Modifier.weight(1f)
                    )
                    QuickAccessItem(
                        title = "Juegos",
                        iconText = "🎮",
                        gradient = listOf(NeonGreen, NeonCyan),
                        onClick = onNavigateToGames,
                        modifier = Modifier.weight(1f)
                    )
                    QuickAccessItem(
                        title = "Traductor",
                        iconText = "🌐",
                        gradient = listOf(NeonPurple, NeonPink),
                        onClick = onNavigateToTranslator,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Module Cards
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 400.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(modules) { module ->
                        ModuleCardItem(
                            module = module,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Recent Activity Section
                Text(
                    text = "Actividad Reciente",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    recentActivities.forEach { (text, time) ->
                        RecentActivityItem(text = text, time = time)
                    }
                }

                // Footer
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "MiniAmigixV  •  Versión 1.0  •  © 2026",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            if (showSuggestionsDialog) {
                NeonSuggestionsDialog(
                    onDismiss = { showSuggestionsDialog = false },
                    onSubmit = { showSuggestionsDialog = false }
                )
            }
        }
    }
}

@Composable
private fun FraseDelDiaCard(modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier) {
        Column {
            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = NeonOrange)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Frase del Día", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "\"El único modo de hacer un gran trabajo es amar lo que haces.\"",
                color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("- Steve Jobs", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
        }
    }
}

@Composable
private fun TuActividadCard(modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier) {
        Column {
            Text("Tu Actividad", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                KpiItem("💬", "0", "Chats")
                KpiItem("🎵", "0", "Canciones")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                KpiItem("📅", "0", "Eventos")
                KpiItem("🌦", "23°", "Clima")
            }
        }
    }
}

@Composable
private fun KpiItem(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 20.sp)
        Text(
            text = value,
            color = NeonCyan,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun QuickAccessItem(
    title: String,
    iconText: String,
    gradient: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier.clickable { onClick() }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = iconText, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun RecentActivityItem(text: String, time: String) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(NeonPurple.copy(alpha = 0.2f), NeonBlue.copy(alpha = 0.15f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                val icon = when {
                    text.contains("Chat") -> Icons.Filled.SmartToy
                    text.contains("Música") || text.contains("Music") -> Icons.Filled.LibraryMusic
                    text.contains("Evento") -> Icons.Filled.Event
                    text.contains("Traductor") -> Icons.Filled.Translate
                    else -> Icons.Filled.CheckCircle
                }
                Icon(icon, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Text(time, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }
            Text("✔", color = NeonGreen, fontSize = 18.sp)
        }
    }
}

@Composable
private fun ModuleCardItem(
    module: ModuleCard,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f, label = "scale")
    
    var glowAlpha by remember { mutableStateOf(0.1f) }
    LaunchedEffect(Unit) {
        while (true) {
            glowAlpha = 0.3f
            delay(1000)
            glowAlpha = 0.1f
            delay(1000)
        }
    }

    NeonGlowBox(
        glowColor = module.iconColor,
        modifier = modifier.scale(scale)
    ) {
        GlassCard(
            modifier = Modifier
                .clickable(interactionSource = interactionSource, indication = null, onClick = module.onClick)
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    module.iconColor.copy(alpha = glowAlpha),
                                    Color.Transparent
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(module.icon, contentDescription = null, tint = module.iconColor, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(module.title, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(module.description, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, lineHeight = 16.sp)
            }
        }
    }
}

@Composable
private fun NeonSuggestionsDialog(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    var suggestion by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = NeonPurple)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sugerencias",
                        color = NeonCyan,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = MaterialTheme.colorScheme.onBackground)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF09090B))
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF09090B))
                            .padding(12.dp)
                    ) {
                        OutlinedTextField(
                            value = suggestion,
                            onValueChange = { suggestion = it },
                            placeholder = { 
                                Text(
                                    "¿Cómo podemos mejorar MiniAmigixV?",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha=0.6f),
                                    fontSize = 14.sp
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                cursorColor = NeonCyan,
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        Icon(
                            Icons.Filled.SmartToy,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(20.dp)
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Transparent)
                            .padding(1.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawRoundRect(
                                color = NeonPurple,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f),
                                cornerRadius = CornerRadius(8.dp.toPx())
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(NeonPurple, NeonCyan)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Enviar al Admin", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

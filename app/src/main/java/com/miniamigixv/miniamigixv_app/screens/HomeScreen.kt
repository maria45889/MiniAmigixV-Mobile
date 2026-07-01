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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import com.miniamigixv.miniamigixv_app.R

// Neon accent colors
private val NeonPurple = Color(0xFF8B5CF6)
private val NeonBlue = Color(0xFF3b82f6)
private val NeonCyan = Color(0xFF22D3EE)
private val NeonPink = Color(0xFFec4899)
private val NeonGreen = Color(0xFF10b981)
private val NeonOrange = Color(0xFFf59e0b)
private val ErrorRed = Color(0xFFef4444)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String = "María José",
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
    var isDarkMode by remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

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
                        
                        // Cerrar Sesión button
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
                        
                        // Gradient Bar
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
                    
                    // Bottom icons row
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
                        IconButton(onClick = { isDarkMode = !isDarkMode }) {
                            Icon(
                                if (isDarkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode, 
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
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
            ) {
                val isCompact = maxWidth < 600.dp
                
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        // Main Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Bienvenido a MiniAmigixV",
                                    fontSize = 26.sp,
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
                            }
                        }
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        if (isCompact) {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                FraseDelDiaCard(Modifier.fillMaxWidth())
                                TuActividadCard(Modifier.fillMaxWidth())
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                FraseDelDiaCard(Modifier.weight(1f))
                                TuActividadCard(Modifier.weight(1f))
                            }
                        }
                    }

                    items(modules) { module ->
                        ModuleCardItem(
                            module = module,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
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
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Column {
            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = NeonOrange)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Frase del Día", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "\"El único modo de hacer un gran trabajo es amar lo que haces.\"",
                color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("- Steve Jobs", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
        }
    }
}

@Composable
private fun TuActividadCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Column {
            Text("Tu Actividad", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            ActivityChart()
        }
    }
}

@Composable
private fun ActivityChart() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        val barWidth = size.width / 8
        val maxHeight = size.height
        val data = listOf(0.8f, 0.2f, 0.5f, 0.9f)
        val colors = listOf(NeonPurple, NeonBlue, NeonPink, NeonGreen)

        data.forEachIndexed { index, value ->
            val barHeight = value * maxHeight
            val x = index * (barWidth * 2) + barWidth / 2
            val y = maxHeight - barHeight

            drawRoundRect(
                color = colors[index],
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(4.dp.toPx())
            )
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
    
    // Glow animation
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(interactionSource = interactionSource, indication = null, onClick = module.onClick)
            .padding(16.dp)
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
                        
                        // Small bot icon bottom right
                        Icon(
                            Icons.Filled.SmartToy,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(20.dp)
                        )
                    }
                    
                    // Purple border effect
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

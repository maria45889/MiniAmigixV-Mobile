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

// Dark Neon Theme Colors
private val BgDark1 = Color(0xFF0F0C29)
private val BgDark2 = Color(0xFF302B63)
private val BgDark3 = Color(0xFF24243E)
private val CardBg = Color(0x1AFFFFFF)
private val DialogBg = Color(0xFF13131A)
private val NeonPurple = Color(0xFFa855f7)
private val NeonBlue = Color(0xFF3b82f6)
private val NeonCyan = Color(0xFF06b6d4)
private val NeonPink = Color(0xFFec4899)
private val NeonGreen = Color(0xFF10b981)
private val NeonOrange = Color(0xFFf59e0b)
private val TextWhite = Color(0xFFFFFFFF)
private val TextGray = Color(0xFFA0A0A0)
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
    val color: Color = TextWhite
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
    onLogout: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var showSuggestionsDialog by remember { mutableStateOf(false) }
    var isDarkMode by remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val modules = listOf(
        ModuleCard("Chat IA", "Conversa con tu asistente virtual inteligente.", Icons.Filled.SmartToy, NeonPink, onNavigateToChat),
        ModuleCard("Música", "Escucha tus canciones favoritas.", Icons.Filled.LibraryMusic, NeonBlue, onNavigateToMusic),
        ModuleCard("Juegos", "Diviértete con nuestra colección.", Icons.Filled.SportsEsports, NeonGreen, onNavigateToGames),
        ModuleCard("Estudio", "Herramientas para mejorar tu aprendizaje.", Icons.Filled.School, NeonOrange, onNavigateToStudy),
        ModuleCard("Clima", "Revisa el pronóstico actual.", Icons.Filled.Cloud, NeonBlue, onNavigateToWeather),
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
        MenuItem("Centro de Administración General", Icons.Filled.AdminPanelSettings, onNavigateToAdminCenter, NeonPurple)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = BgDark1,
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
                                    selectedContainerColor = Brush.horizontalGradient(
                                        colors = listOf(NeonBlue.copy(alpha=0.3f), Color.Transparent)
                                    ).let { Color.Transparent } // Compose doesn't support Brush in selectedContainerColor directly, so we just use transparent and build custom if needed. But for simplicity, we'll use a color
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
                    
                    Divider(color = CardBg)
                    
                    // Bottom icons row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onNavigateToProfile) {
                            Icon(Icons.Filled.PersonOutline, contentDescription = "Perfil", tint = TextGray)
                        }
                        IconButton(onClick = { showSuggestionsDialog = true }) {
                            Icon(Icons.Filled.HelpOutline, contentDescription = "Ayuda / Sugerencias", tint = TextGray)
                        }
                        IconButton(onClick = { /* Configuraciones */ }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Configuración", tint = TextGray)
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
                            Text(" / Inicio", color = TextGray, fontSize = 16.sp)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú", tint = TextWhite)
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Notificaciones */ }) {
                            Icon(Icons.Filled.NotificationsNone, contentDescription = "Notificaciones", tint = TextWhite)
                        }
                        IconButton(onClick = { isDarkMode = !isDarkMode }) {
                            Icon(
                                if (isDarkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode, 
                                contentDescription = "Cambiar Tema", 
                                tint = TextWhite
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BgDark1,
                        titleContentColor = TextWhite
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(BgDark1, BgDark2, BgDark3)
                        )
                    )
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Main Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(CardBg)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Bienvenido a MiniAmigixV",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonBlue,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tu espacio personal interactivo",
                                fontSize = 16.sp,
                                color = TextGray,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Filled.Download, contentDescription = null, tint = TextWhite)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Instalar App", color = TextWhite)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Two cards row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Frase del Día
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(CardBg)
                                .padding(16.dp)
                        ) {
                            Column {
                                Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = NeonOrange)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Frase del Día", color = TextWhite, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "\"El único modo de hacer un gran trabajo es amar lo que haces.\"",
                                    color = TextGray, fontSize = 12.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("- Steve Jobs", color = TextGray, fontSize = 10.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
                            }
                        }

                        // Actividad
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(CardBg)
                                .padding(16.dp)
                        ) {
                            Column {
                                Text("Tu Actividad", color = TextWhite, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(16.dp))
                                ActivityChart()
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Modules Grid
                    modules.chunked(2).forEach { rowModules ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            rowModules.forEach { module ->
                                ModuleCardItem(
                                    module = module,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowModules.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Spacer(modifier = Modifier.height(32.dp))
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

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg)
            .clickable(interactionSource = interactionSource, indication = null, onClick = module.onClick)
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(module.icon, contentDescription = null, tint = module.iconColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(module.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(module.description, color = TextGray, fontSize = 12.sp, lineHeight = 16.sp)
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
                .background(DialogBg)
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
                        Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = TextWhite)
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
                                    color = TextGray.copy(alpha=0.6f),
                                    fontSize = 14.sp
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                cursorColor = NeonCyan,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
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
                        Text("Enviar al Admin", color = TextWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

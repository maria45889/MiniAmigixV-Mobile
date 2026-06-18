package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class ModuleCard(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: CardColor,
    val onClick: () -> Unit
)

private data class CardColor(
    val container: Color,
    val content: Color
)

private data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
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
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val modules = listOf(
        ModuleCard(
            title = "Chat IA",
            description = "Conversa y explora con tu asistente virtual inteligente.",
            icon = Icons.Filled.SmartToy,
            color = CardColor(
                container = MaterialTheme.colorScheme.primaryContainer,
                content = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onClick = onNavigateToChat
        ),
        ModuleCard(
            title = "Música",
            description = "Escucha tus canciones favoritas en un solo lugar.",
            icon = Icons.Filled.LibraryMusic,
            color = CardColor(
                container = MaterialTheme.colorScheme.secondaryContainer,
                content = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            onClick = onNavigateToMusic
        ),
        ModuleCard(
            title = "Juegos",
            description = "Diviértete y relájate con nuestra colección de juegos.",
            icon = Icons.Filled.SportsEsports,
            color = CardColor(
                container = MaterialTheme.colorScheme.tertiaryContainer,
                content = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            onClick = onNavigateToGames
        ),
        ModuleCard(
            title = "Estudio",
            description = "Herramientas y recursos para mejorar tu aprendizaje.",
            icon = Icons.Filled.School,
            color = CardColor(
                container = MaterialTheme.colorScheme.surfaceVariant,
                content = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            onClick = onNavigateToStudy
        ),
        ModuleCard(
            title = "Clima",
            description = "Revisa el pronóstico y las condiciones actuales.",
            icon = Icons.Filled.Cloud,
            color = CardColor(
                container = MaterialTheme.colorScheme.primaryContainer,
                content = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            onClick = onNavigateToWeather
        ),
        ModuleCard(
            title = "Blog",
            description = "Lee, escribe y comparte tus ideas con la comunidad.",
            icon = Icons.Filled.Article,
            color = CardColor(
                container = MaterialTheme.colorScheme.secondaryContainer,
                content = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            onClick = onNavigateToBlog
        )
    )

    val menuItems = listOf(
        MenuItem("Inicio", Icons.Filled.Home) { },
        MenuItem("Chat IA", Icons.Filled.SmartToy, onNavigateToChat),
        MenuItem("Música", Icons.Filled.LibraryMusic, onNavigateToMusic),
        MenuItem("Juegos", Icons.Filled.SportsEsports, onNavigateToGames),
        MenuItem("Estudio", Icons.Filled.School, onNavigateToStudy),
        MenuItem("Eventos", Icons.Filled.Event, onNavigateToEvents),
        MenuItem("Clima", Icons.Filled.Cloud, onNavigateToWeather),
        MenuItem("Traductor", Icons.Filled.Translate, onNavigateToTranslator),
        MenuItem("Entretenimiento", Icons.Filled.Movie, onNavigateToEntertainment),
        MenuItem("Blog", Icons.Filled.Article, onNavigateToBlog),
        MenuItem("Soporte", Icons.Filled.SupportAgent, onNavigateToSupport),
        MenuItem("Centro de Administración", Icons.Filled.AdminPanelSettings, onNavigateToAdminCenter),
        MenuItem("Cerrar Sesión", Icons.Filled.Logout, onLogout)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "MiniAmigixV",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Menú de navegación",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    menuItems.forEach { item ->
                        NavigationDrawerItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = false,
                            onClick = {
                                item.onClick()
                                coroutineScope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("MiniAmigixV") },
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Bienvenido a MiniAmigixV",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Tu espacio personal interactivo",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.InstallMobile, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Instalar App")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Frase del Día",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "\"La vida es un 10% lo que me pasa y un 90% cómo reacciono a ello.\"",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "- Charles R. Swindoll",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Tu Actividad",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            ActivityChart(
                                primaryColor = MaterialTheme.colorScheme.primary,
                                secondaryColor = MaterialTheme.colorScheme.secondary,
                                tertiaryColor = MaterialTheme.colorScheme.tertiary,
                                errorColor = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Explora MiniAmigixV",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                modules.chunked(2).forEach { rowModules ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowModules.forEach { module ->
                            ModuleCardItem(
                                module = module,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (showSuggestionsDialog) {
                    SuggestionsDialog(
                        onDismiss = { showSuggestionsDialog = false },
                        onSubmit = { showSuggestionsDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityChart(
    primaryColor: Color,
    secondaryColor: Color,
    tertiaryColor: Color,
    errorColor: Color
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        val barWidth = size.width / 4
        val maxHeight = size.height
        val data = listOf(0.7f, 0.5f, 0.8f, 0.4f)
        val colors = listOf(primaryColor, secondaryColor, tertiaryColor, errorColor)

        data.forEachIndexed { index, value ->
            val barHeight = value * maxHeight
            val x = index * barWidth + barWidth / 4
            val y = maxHeight - barHeight

            drawRoundRect(
                color = colors[index],
                topLeft = Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth / 2f, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
            )
        }
    }
}

@Composable
private fun SuggestionsDialog(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    var suggestion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sugerencias") },
        text = {
            Column {
                Text("¿Cómo podemos mejorar MiniAmigixV?")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = suggestion,
                    onValueChange = { suggestion = it },
                    placeholder = { Text("Escribe tu sugerencia...") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = onSubmit) {
                Text("Enviar al Admin")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun ModuleCardItem(
    module: ModuleCard,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )

    ElevatedCard(
        onClick = module.onClick,
        interactionSource = interactionSource,
        modifier = modifier
            .scale(scale)
            .clip(MaterialTheme.shapes.extraLarge),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = module.color.container,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = module.icon,
                        contentDescription = module.title,
                        tint = module.color.content,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = module.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = module.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

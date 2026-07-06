package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.miniamigixv.miniamigixv_app.ui.theme.*

private data class NotificationItem(
    val id: Int,
    val title: String,
    val message: String,
    val timeAgo: String,
    val category: String,
    val isRead: Boolean,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(themeViewModel: ThemeViewModel, onBack: () -> Unit) {
    var selectedFilter by remember { mutableStateOf("todas") }
    var searchQuery by remember { mutableStateOf("") }

    val filters = listOf(
        "todas" to "Todas",
        "no-leidas" to "No leídas",
        "chat_ia" to "Chat IA",
        "evento" to "Evento",
        "musica" to "Música",
        "estudio" to "Estudio"
    )

    val filterIcons = mapOf(
        "todas" to Icons.Filled.Notifications,
        "no-leidas" to Icons.Filled.MarkEmailUnread,
        "chat_ia" to Icons.Filled.SmartToy,
        "evento" to Icons.Filled.Event,
        "musica" to Icons.Filled.MusicNote,
        "estudio" to Icons.Filled.Mic
    )

    val categoryColors = mapOf(
        "chat_ia" to NeonViolet,
        "evento" to NeonBlue,
        "musica" to NeonPink,
        "estudio" to NeonGreen
    )

    val allNotifications = remember {
        listOf(
            NotificationItem(1, "MiniAmigixV te respondió", "¡Hola! Claro que sí, cuéntame más sobre tu proyecto...", "hace 2 min", "chat_ia", false, Icons.Filled.SmartToy),
            NotificationItem(2, "Nuevo evento: Jam Session", "Este viernes a las 7pm en el estudio. ¡No faltes!", "hace 15 min", "evento", false, Icons.Filled.Event),
            NotificationItem(3, "Canción recomendada", "Basado en tu escucha: 'Blinding Lights' - The Weeknd", "hace 1 h", "musica", false, Icons.Filled.MusicNote),
            NotificationItem(4, "Proyecto actualizado", "Tu beat 'Noche de Verano' tiene una nueva versión.", "hace 3 h", "estudio", false, Icons.Filled.Mic),
            NotificationItem(5, "MiniAmigixV te respondió", "Me encanta tu energía. ¿Qué género quieres explorar?", "hace 5 h", "chat_ia", true, Icons.Filled.SmartToy),
            NotificationItem(6, "Recordatorio de evento", "Taller de producción musical mañana a las 4pm.", "hace 1 d", "evento", true, Icons.Filled.Event),
            NotificationItem(7, "Nueva canción en tendencias", "'Flowers' - Miley Cyrus está en el top 10 esta semana.", "hace 2 d", "musica", true, Icons.Filled.MusicNote),
            NotificationItem(8, "Estudio: mezcla lista", "La mezcla de 'Amanecer' está lista para revisión.", "hace 3 d", "estudio", true, Icons.Filled.Mic)
        )
    }

    val filteredNotifications = allNotifications.filter { n ->
        val matchesFilter = when (selectedFilter) {
            "no-leidas" -> !n.isRead
            else -> selectedFilter == "todas" || n.category == selectedFilter
        }
        val matchesSearch = searchQuery.isEmpty() ||
                n.title.contains(searchQuery, ignoreCase = true) ||
                n.message.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }

    val unreadCount = allNotifications.count { !it.isRead }

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
                        "Centro de Notificaciones",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(NeonViolet.copy(alpha = 0.15f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                "$unreadCount no leídas",
                                color = NeonViolet,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "${allNotifications.size} total",
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 12.sp
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(
                            onClick = { },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Filled.Refresh,
                                contentDescription = "Actualizar",
                                tint = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(
                            onClick = { },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Filled.DoneAll,
                                contentDescription = "Marcar leídas",
                                tint = NeonBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(
                            onClick = { },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Filled.DeleteSweep,
                                contentDescription = "Limpiar leídas",
                                tint = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    placeholder = {
                        Text(
                            "Buscar notificaciones...",
                            color = Color.White.copy(alpha = 0.3f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.4f)
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonViolet.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = NeonViolet,
                        focusedContainerColor = Color.White.copy(alpha = 0.04f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.04f)
                    ),
                    shape = RoundedCornerShape(14.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filters.size) { index ->
                        val key = filters[index].first
                        val label = filters[index].second
                        val selected = selectedFilter == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (selected) NeonViolet.copy(alpha = 0.2f)
                                    else Color.White.copy(alpha = 0.05f)
                                )
                                .clickable { selectedFilter = key }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    filterIcons[key] ?: Icons.Filled.Notifications,
                                    contentDescription = null,
                                    tint = if (selected) NeonViolet else Color.White.copy(alpha = 0.5f),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    label,
                                    color = if (selected) NeonViolet else Color.White.copy(alpha = 0.6f),
                                    fontSize = 12.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredNotifications) { notification ->
                    val catColor = categoryColors[notification.category] ?: NeonViolet

                    GlassCard(
                        modifier = if (!notification.isRead) {
                            Modifier.background(
                                catColor.copy(alpha = 0.04f),
                                RoundedCornerShape(20.dp)
                            )
                        } else Modifier
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(catColor.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    notification.icon,
                                    contentDescription = null,
                                    tint = catColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        notification.title,
                                        color = Color.White,
                                        fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 13.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (!notification.isRead) {
                                        Box(
                                            modifier = Modifier
                                                .size(7.dp)
                                                .clip(RoundedCornerShape(50))
                                                .background(NeonBlue)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    notification.message,
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 12.sp,
                                    maxLines = 2
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        notification.timeAgo,
                                        color = Color.White.copy(alpha = 0.3f),
                                        fontSize = 10.sp
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(catColor.copy(alpha = 0.15f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            notification.category.replace("_", " ").uppercase(),
                                            color = catColor,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

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
    val icon: ImageVector,
    val priority: String = "normal",
    val isPinned: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val link: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(themeViewModel: ThemeViewModel, onBack: () -> Unit) {
    var selectedFilter by remember { mutableStateOf("todas") }
    var searchQuery by remember { mutableStateOf("") }

    val filters = listOf(
        "todas" to "Todas",
        "destacadas" to "Destacadas",
        "no-leidas" to "No leídas",
        "chat_ia" to "Chat IA",
        "evento" to "Evento",
        "musica" to "Música",
        "estudio" to "Estudio",
        "soporte" to "Soporte",
        "sistema" to "Sistema"
    )

    val filterIcons = mapOf(
        "todas" to Icons.Filled.Notifications,
        "destacadas" to Icons.Filled.Star,
        "no-leidas" to Icons.Filled.MarkEmailUnread,
        "chat_ia" to Icons.Filled.SmartToy,
        "evento" to Icons.Filled.Event,
        "musica" to Icons.Filled.MusicNote,
        "estudio" to Icons.Filled.Mic,
        "soporte" to Icons.Filled.SupportAgent,
        "sistema" to Icons.Filled.Settings
    )

    val categoryColors = mapOf(
        "chat_ia" to NeonViolet,
        "evento" to NeonBlue,
        "musica" to NeonPink,
        "estudio" to NeonGreen,
        "soporte" to Color(0xFFF59E0B),
        "sistema" to Color(0xFF6B7280)
    )

    val allNotifications = remember {
        val now = System.currentTimeMillis()
        listOf(
            NotificationItem(1, "MiniAmigixV te respondió", "¡Hola! Claro que sí, cuéntame más sobre tu proyecto...", "hace 2 min", "chat_ia", false, Icons.Filled.SmartToy, "alta", false, now - 2 * 60 * 1000, "/chat/"),
            NotificationItem(2, "Nuevo evento: Jam Session", "Este viernes a las 7pm en el estudio. ¡No faltes!", "hace 15 min", "evento", false, Icons.Filled.Event, "alta", true, now - 15 * 60 * 1000, "/eventos/"),
            NotificationItem(3, "Canción recomendada", "Basado en tu escucha: 'Blinding Lights' - The Weeknd", "hace 1 h", "musica", false, Icons.Filled.MusicNote, "normal", false, now - 60 * 60 * 1000, "/musica/"),
            NotificationItem(4, "Proyecto actualizado", "Tu beat 'Noche de Verano' tiene una nueva versión.", "hace 3 h", "estudio", false, Icons.Filled.Mic, "normal", false, now - 3 * 60 * 60 * 1000, "/estudio/"),
            NotificationItem(5, "MiniAmigixV te respondió", "Me encanta tu energía. ¿Qué género quieres explorar?", "hace 5 h", "chat_ia", true, Icons.Filled.SmartToy, "normal", false, now - 5 * 60 * 60 * 1000, "/chat/"),
            NotificationItem(6, "Recordatorio de evento", "Taller de producción musical mañana a las 4pm.", "hace 1 d", "evento", true, Icons.Filled.Event, "alta", false, now - 24 * 60 * 60 * 1000, "/eventos/"),
            NotificationItem(7, "Nueva canción en tendencias", "'Flowers' - Miley Cyrus está en el top 10 esta semana.", "hace 2 d", "musica", true, Icons.Filled.MusicNote, "baja", false, now - 2 * 24 * 60 * 60 * 1000, "/musica/"),
            NotificationItem(8, "Estudio: mezcla lista", "La mezcla de 'Amanecer' está lista para revisión.", "hace 3 d", "estudio", true, Icons.Filled.Mic, "normal", false, now - 3 * 24 * 60 * 60 * 1000, "/estudio/")
        )
    }

    val filteredNotifications = allNotifications.filter { n ->
        val matchesFilter = when (selectedFilter) {
            "no-leidas" -> !n.isRead
            "destacadas" -> n.priority == "alta" || n.isPinned
            else -> selectedFilter == "todas" || n.category == selectedFilter
        }
        val matchesSearch = searchQuery.isEmpty() ||
                n.title.contains(searchQuery, ignoreCase = true) ||
                n.message.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }.sortedWith(compareByDescending<NotificationItem> { it.isPinned }.thenByDescending { it.priority == "alta" }.thenByDescending { it.timestamp })

    val unreadCount = allNotifications.count { !it.isRead }

    // Group notifications by date
    val notificationsByDate = remember(filteredNotifications) {
        val now = System.currentTimeMillis()
        val todayStart = now - (now % (24 * 60 * 60 * 1000))
        val yesterdayStart = todayStart - (24 * 60 * 60 * 1000)
        val weekStart = todayStart - (7 * 24 * 60 * 60 * 1000)
        val monthStart = todayStart - (30 * 24 * 60 * 60 * 1000)

        filteredNotifications.groupBy { notif ->
            when {
                notif.timestamp >= todayStart -> "Hoy"
                notif.timestamp >= yesterdayStart -> "Ayer"
                notif.timestamp >= weekStart -> "Esta semana"
                notif.timestamp >= monthStart -> "Este mes"
                else -> {
                    val date = java.util.Date(notif.timestamp)
                    java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale("es")).format(date)
                }
            }
        }
    }

    // Statistics by category
    val statsByCategory = remember(allNotifications) {
        allNotifications.groupBy { it.category }.mapValues { it.value.size }
    }

    // Featured notifications (high priority or pinned)
    val featuredNotifications = remember(allNotifications) {
        allNotifications.filter { it.priority == "alta" || it.isPinned }.take(3)
    }

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

                // Featured notifications section
                if (featuredNotifications.isNotEmpty() && selectedFilter == "todas") {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "⭐ Destacadas",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    featuredNotifications.forEach { notif ->
                        FeaturedNotificationCard(
                            notification = notif,
                            categoryColor = categoryColors[notif.category] ?: NeonViolet
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Statistics by category
                if (selectedFilter == "todas" && statsByCategory.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "📊 Estadísticas",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(statsByCategory.keys.toList()) { category ->
                            val count = statsByCategory[category] ?: 0
                            val color = categoryColors[category] ?: NeonViolet
                            val icon = filterIcons[category] ?: Icons.Filled.Notifications
                            StatBadge(
                                category = category,
                                count = count,
                                color = color,
                                icon = icon
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Show notifications grouped by date
                notificationsByDate.forEach { (dateGroup, notifications) ->
                    item {
                        Text(
                            dateGroup,
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(notifications) { notification ->
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
                                        if (notification.isPinned) {
                                            Icon(
                                                Icons.Filled.PushPin,
                                                contentDescription = "Fijado",
                                                tint = Color(0xFFF59E0B),
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }
                                        Text(
                                            notification.title,
                                            color = Color.White,
                                            fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 13.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (notification.priority == "alta") {
                                            Text(
                                                "🔥",
                                                modifier = Modifier.padding(start = 4.dp)
                                            )
                                        }
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
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    IconButton(
                                        onClick = { /* TODO: Mark as read */ },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            if (notification.isRead) Icons.Filled.MarkEmailRead else Icons.Filled.MarkEmailUnread,
                                            contentDescription = "Marcar leída",
                                            tint = Color.White.copy(alpha = 0.4f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { /* TODO: Pin/Unpin */ },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            if (notification.isPinned) Icons.Filled.PushPin else Icons.Filled.PushPin,
                                            contentDescription = "Fijar",
                                            tint = if (notification.isPinned) Color(0xFFF59E0B) else Color.White.copy(alpha = 0.4f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { /* TODO: Delete */ },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.Delete,
                                            contentDescription = "Eliminar",
                                            tint = Color(0xFFEF4444).copy(alpha = 0.6f),
                                            modifier = Modifier.size(16.dp)
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

@Composable
private fun FeaturedNotificationCard(
    notification: NotificationItem,
    categoryColor: Color
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                categoryColor.copy(alpha = 0.08f),
                RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(categoryColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    notification.icon,
                    contentDescription = null,
                    tint = categoryColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        notification.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                    if (notification.priority == "alta") {
                        Text(
                            "🔥",
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
                Text(
                    notification.message,
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }
            Text(
                notification.timeAgo,
                color = Color.White.copy(alpha = 0.3f),
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun StatBadge(
    category: String,
    count: Int,
    color: Color,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(12.dp)
            )
            Text(
                "$count",
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

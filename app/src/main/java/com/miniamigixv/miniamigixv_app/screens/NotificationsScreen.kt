package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.*

private data class Notification(
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean,
    val type: String // "all", "alert", "system", "warning"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(onBack: () -> Unit = {}) {
    var selectedFilter by remember { mutableStateOf("Todas") }
    val filters = listOf("Todas", "No leídas", "Alertas", "Sistema", "Advertencias")

    val notifications = listOf(
        Notification("Nueva respuesta del Chat IA", "MiniAmigixV ha respondido: \"¡Oh, claro, que buenas actitudes esto como tu compañera de la escuela...\"", "04 Jun 2026, 19:52", false, "alert"),
        Notification("Nueva respuesta del Chat IA", "MiniAmigixV ha respondido: \"...hola, la idea que sigas adelante con tu proyecto...\"", "04 Jun 2026, 15:14", false, "alert"),
        Notification("Nueva respuesta del Chat IA", "MiniAmigixV ha respondido: \"Hoy MiniAmigixV, el asistente de inteligencia artificial es más...\"", "04 Jun 2026, 13:30", true, "alert"),
        Notification("Actualización del sistema", "Se han aplicado mejoras de rendimiento y seguridad.", "03 Jun 2026, 10:00", true, "system"),
        Notification("Nueva respuesta del Chat IA", "MiniAmigixV ha respondido: \"¿Cómo claro puede compartir su historia para...\"", "02 Jun 2026, 09:12", true, "alert"),
        Notification("Advertencia de sesión", "Se detectó un inicio de sesión desde otro dispositivo.", "01 Jun 2026, 22:45", true, "warning")
    )

    val filteredNotifications = when (selectedFilter) {
        "No leídas" -> notifications.filter { !it.isRead }
        "Alertas" -> notifications.filter { it.type == "alert" }
        "Sistema" -> notifications.filter { it.type == "system" }
        "Advertencias" -> notifications.filter { it.type == "warning" }
        else -> notifications
    }

    Column(modifier = Modifier.fillMaxSize().background(BgDark)) {
        TopAppBar(
            title = { Text("Notificaciones", fontWeight = FontWeight.Bold, color = TextWhite) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = TextWhite)
                }
            },
            actions = {
                IconButton(onClick = { /* Mark all read */ }) {
                    Icon(Icons.Filled.DoneAll, contentDescription = "Marcar todo leído", tint = NeonCyan)
                }
                IconButton(onClick = { /* Delete all */ }) {
                    Icon(Icons.Filled.DeleteSweep, contentDescription = "Eliminar todo", tint = TextGray)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBg)
        )

        // Filter chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                NeonChip(
                    text = filter,
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter }
                )
            }
        }

        // Notifications list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredNotifications.size) { index ->
                val notification = filteredNotifications[index]
                NotificationCard(notification)
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
private fun NotificationCard(notification: Notification) {
    val borderColor = if (!notification.isRead) NeonCyan.copy(alpha = 0.6f) else Color.Transparent

    NeonCard(padding = 14.dp, modifier = if (!notification.isRead) {
        Modifier.background(NeonCyan.copy(alpha = 0.03f), RoundedCornerShape(16.dp))
    } else Modifier) {
        Row {
            // Unread indicator
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(NeonCyan)
                        .align(Alignment.Top)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Notifications,
                        contentDescription = null,
                        tint = if (!notification.isRead) NeonCyan else TextGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        notification.title,
                        color = TextWhite,
                        fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(notification.message, color = TextGray, fontSize = 12.sp, maxLines = 2)
                Spacer(modifier = Modifier.height(6.dp))
                Text(notification.time, color = TextGray.copy(alpha = 0.6f), fontSize = 10.sp)
            }

            IconButton(onClick = { /* Delete */ }) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF4444).copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
            }
        }
    }
}

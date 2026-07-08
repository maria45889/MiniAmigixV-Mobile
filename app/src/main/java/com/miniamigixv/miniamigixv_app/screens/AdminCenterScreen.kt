package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.GlassCard
import com.miniamigixv.miniamigixv_app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCenterScreen(onBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf("dashboard") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Centro de Administración") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF09090F), Color(0xFF0F172A))
                    )
                )
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Tabs
                ScrollableTabRow(
                    selectedTabIndex = when (selectedTab) {
                        "dashboard" -> 0
                        "usuarios" -> 1
                        "notificaciones" -> 2
                        "soporte" -> 3
                        "sugerencias" -> 4
                        else -> 0
                    },
                    containerColor = Color.Transparent,
                    contentColor = NeonViolet
                ) {
                    Tab(
                        selected = selectedTab == "dashboard",
                        onClick = { selectedTab = "dashboard" },
                        text = { Text("Dashboard") }
                    )
                    Tab(
                        selected = selectedTab == "usuarios",
                        onClick = { selectedTab = "usuarios" },
                        text = { Text("Usuarios") }
                    )
                    Tab(
                        selected = selectedTab == "notificaciones",
                        onClick = { selectedTab = "notificaciones" },
                        text = { Text("Notificaciones") }
                    )
                    Tab(
                        selected = selectedTab == "soporte",
                        onClick = { selectedTab = "soporte" },
                        text = { Text("Soporte") }
                    )
                    Tab(
                        selected = selectedTab == "sugerencias",
                        onClick = { selectedTab = "sugerencias" },
                        text = { Text("Sugerencias") }
                    )
                }

                // Content
                when (selectedTab) {
                    "dashboard" -> AdminDashboard()
                    "usuarios" -> AdminUsers()
                    "notificaciones" -> AdminNotifications()
                    "soporte" -> AdminSupport()
                    "sugerencias" -> AdminSuggestions()
                }
            }
        }
    }
}

@Composable
private fun AdminDashboard() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "📊 Estadísticas Generales",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Usuarios", "1,234", NeonViolet, Icons.Filled.People)
                StatCard("Eventos", "56", NeonBlue, Icons.Filled.Event)
            }
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("Tickets", "23", Color(0xFFF59E0B), Icons.Filled.SupportAgent)
                StatCard("Sugerencias", "12", Color(0xFF10B981), Icons.Filled.Lightbulb)
            }
        }
        
        item {
            GlassCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "📈 Actividad Reciente",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ActivityItem("Nuevo usuario registrado", "hace 5 min")
                    ActivityItem("Ticket de soporte creado", "hace 15 min")
                    ActivityItem("Sugerencia enviada", "hace 30 min")
                    ActivityItem("Evento creado", "hace 1 hora")
                }
            }
        }
    }
}

@Composable
private fun AdminUsers() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "👥 Gestión de Usuarios",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(5) { index ->
            GlassCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(50))
                                .background(NeonViolet.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Person, contentDescription = null, tint = NeonViolet)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Usuario ${index + 1}", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("user${index + 1}@email.com", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Color.White.copy(alpha = 0.6f))
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF4444).copy(alpha = 0.6f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminNotifications() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "🔔 Gestión de Notificaciones",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            GlassCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Crear Notificación Global",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Mensaje") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
                    ) {
                        Text("Enviar a todos los usuarios")
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminSupport() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "🛠 Tickets de Soporte",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(3) { index ->
            GlassCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ticket #${index + 1}", color = Color.White, fontWeight = FontWeight.Bold)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when (index) {
                                        0 -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                        1 -> Color(0xFFF59E0B).copy(alpha = 0.2f)
                                        else -> Color(0xFF10B981).copy(alpha = 0.2f)
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                when (index) {
                                    0 -> "Alta"
                                    1 -> "Media"
                                    else -> "Resuelto"
                                },
                                color = when (index) {
                                    0 -> Color(0xFFEF4444)
                                    1 -> Color(0xFFF59E0B)
                                    else -> Color(0xFF10B981)
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Problema con inicio de sesión", color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
                        ) {
                            Text("Responder")
                        }
                        OutlinedButton(onClick = {}) {
                            Text("Ver detalles")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminSuggestions() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "💡 Sugerencias deUsuarios",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(3) { index ->
            GlassCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Sugerencia #${index + 1}", color = Color.White, fontWeight = FontWeight.Bold)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when (index) {
                                        0 -> Color(0xFF10B981).copy(alpha = 0.2f)
                                        1 -> Color(0xFFF59E0B).copy(alpha = 0.2f)
                                        else -> Color(0xFF6B7280).copy(alpha = 0.2f)
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                when (index) {
                                    0 -> "Aprobada"
                                    1 -> "En revisión"
                                    else -> "Pendiente"
                                },
                                color = when (index) {
                                    0 -> Color(0xFF10B981)
                                    1 -> Color(0xFFF59E0B)
                                    else -> Color(0xFF6B7280)
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Mejorar la interfaz de chat", color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Text("Aprobar")
                        }
                        OutlinedButton(onClick = {}) {
                            Text("Responder")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    GlassCard(
        modifier = Modifier
            .weight(1f)
            .background(color.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
        }
    }
}

@Composable
private fun ActivityItem(text: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, color = Color.White.copy(alpha = 0.7f))
        Text(time, color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp)
    }
}

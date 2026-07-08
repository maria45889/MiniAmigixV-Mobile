package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miniamigixv.miniamigixv_app.screens.data.model.Event
import com.miniamigixv.miniamigixv_app.ui.components.GlassCard
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

private val NeonPurple = Color(0xFF8B5CF6)
private val NeonBlue = Color(0xFF06B6D4)
private val NeonCyan = Color(0xFF22D3EE)
private val NeonGreen = Color(0xFF10B981)
private val NeonOrange = Color(0xFFF59E0B)

private val categoryInfo = listOf(
    "Evento" to "\uD83D\uDCC5",
    "Tarea" to "\uD83D\uDCDA",
    "Cumpleaños" to "\uD83C\uDF82",
    "Reunión" to "\uD83E\uDD1D",
    "Recordatorio" to "\uD83D\uDD14"
)

private val categoryColors = listOf(
    NeonPurple, NeonBlue, NeonGreen, NeonCyan, NeonOrange
)

private fun inferCategory(title: String, description: String): Int {
    val lower = (title + " " + description).lowercase()
    return when {
        "cumple" in lower || "birthday" in lower -> 2
        "tarea" in lower || "estudi" in lower || "task" in lower || "deber" in lower -> 1
        "reuni" in lower || "meeting" in lower -> 3
        "record" in lower || "recordatorio" in lower || "reminder" in lower -> 4
        else -> 0
    }
}

private fun categoryForEvent(event: Event): Pair<String, String> {
    val idx = inferCategory(event.title, event.description)
    return categoryInfo[idx]
}

private fun categoryColorForEvent(event: Event): Color {
    val idx = inferCategory(event.title, event.description)
    return categoryColors[idx]
}

private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatDay(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private fun formatMonth(timestamp: Long): String {
    val months = arrayOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")
    val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
    return months[cal.get(Calendar.MONTH)]
}

private fun formatDateShort(timestamp: Long): String {
    val sdf = SimpleDateFormat("d MMM", Locale("es"))
    return sdf.format(Date(timestamp))
}

private fun isToday(timestamp: Long): Boolean {
    val cal = Calendar.getInstance()
    val eventCal = Calendar.getInstance().apply { timeInMillis = timestamp }
    return cal.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
           cal.get(Calendar.DAY_OF_YEAR) == eventCal.get(Calendar.DAY_OF_YEAR)
}

private fun isThisWeek(timestamp: Long): Boolean {
    val cal = Calendar.getInstance()
    val eventCal = Calendar.getInstance().apply { timeInMillis = timestamp }
    return cal.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
           cal.get(Calendar.WEEK_OF_YEAR) == eventCal.get(Calendar.WEEK_OF_YEAR)
}

private fun isThisMonth(timestamp: Long): Boolean {
    val cal = Calendar.getInstance()
    val eventCal = Calendar.getInstance().apply { timeInMillis = timestamp }
    return cal.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
           cal.get(Calendar.MONTH) == eventCal.get(Calendar.MONTH)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    onBack: () -> Unit = {},
    eventsViewModel: EventsViewModel = viewModel()
) {
    val events = eventsViewModel.events
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(-1) }
    var now by remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = System.currentTimeMillis()
            delay(1000)
        }
    }

    val todayCount = events.count { isToday(it.dateTime) }
    val weekCount = events.count { isThisWeek(it.dateTime) }
    val monthCount = events.count { isThisMonth(it.dateTime) }

    val nextEvent = events
        .filter { it.dateTime >= now }
        .minByOrNull { it.dateTime }

    val filteredEvents = events
        .filter { event ->
            searchQuery.isEmpty() || event.title.contains(searchQuery, ignoreCase = true) ||
            event.description.contains(searchQuery, ignoreCase = true)
        }
        .filter { event ->
            selectedCategory == -1 || inferCategory(event.title, event.description) == selectedCategory
        }
        .sortedBy { it.dateTime }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Organizador Personal", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { eventsViewModel.toggleCreateEventDialog(true) },
                containerColor = NeonPurple,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Nuevo Evento")
            }
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF09090F), Color(0xFF0F172A))
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    StatsRow(todayCount, weekCount, monthCount)
                }

                if (nextEvent != null) {
                    item {
                        NextEventHighlightCard(event = nextEvent, now = now)
                    }
                }

                item {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it }
                    )
                }

                item {
                    CategoryFilterTabs(
                        selectedIndex = selectedCategory,
                        onSelect = { selectedCategory = it }
                    )
                }

                item {
                    Text(
                        text = "\u23F0 Pr\u00F3ximos eventos",
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                if (filteredEvents.isEmpty()) {
                    item {
                        EmptyState()
                    }
                } else {
                    items(filteredEvents, key = { it.id }) { event ->
                        EventCard(
                            event = event,
                            onDelete = { eventsViewModel.deleteEvent(event.id) }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }

    if (eventsViewModel.showCreateEventDialog) {
        CreateEventDialog(
            onDismiss = { eventsViewModel.toggleCreateEventDialog(false) },
            onConfirm = { title, description, dateTime, location ->
                eventsViewModel.addEvent(title, description, dateTime, location)
            }
        )
    }
}

@Composable
private fun StatsRow(todayCount: Int, weekCount: Int, monthCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(
            count = todayCount,
            label = "Hoy",
            emoji = "\uD83D\uDCC5",
            color = NeonPurple,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            count = weekCount,
            label = "Semana",
            emoji = "\uD83D\uDCC6",
            color = NeonCyan,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            count = monthCount,
            label = "Mes",
            emoji = "\uD83D\uDDD3\uFE0F",
            color = NeonGreen,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    count: Int,
    label: String,
    emoji: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = count.toString(),
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun NextEventHighlightCard(event: Event, now: Long) {
    val diff = max(0L, event.dateTime - now)
    val days = diff / (1000 * 60 * 60 * 24)
    val hours = (diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
    val minutes = (diff % (1000 * 60 * 60)) / (1000 * 60)

    val (catEmoji, _) = categoryForEvent(event)
    val catColor = categoryColorForEvent(event)

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "\u23F0 Pr\u00F3ximo evento",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(catEmoji, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        event.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        "${formatDateShort(event.dateTime)} - ${formatTime(event.dateTime)}",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                    if (event.location.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "\uD83D\uDCCD ${event.location}",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CountdownBlock(value = days.toInt(), label = "d\u00EDas", color = catColor)
                CountdownBlock(value = hours.toInt(), label = "horas", color = catColor)
                CountdownBlock(value = minutes.toInt(), label = "min", color = catColor)
            }
        }
    }
}

@Composable
private fun CountdownBlock(value: Int, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value.toString().padStart(2, '0'),
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            label,
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 10.sp
        )
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Filled.Search,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        "Buscar evento...",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 14.sp
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontSize = 14.sp
                ),
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = NeonPurple,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Limpiar",
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryFilterTabs(selectedIndex: Int, onSelect: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedIndex == -1,
            onClick = { onSelect(-1) },
            label = { Text("Todos", fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = NeonPurple.copy(alpha = 0.3f),
                containerColor = Color.White.copy(alpha = 0.05f),
                selectedLabelColor = Color.White,
                labelColor = Color.White.copy(alpha = 0.7f)
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (selectedIndex == -1) NeonPurple.copy(alpha = 0.5f)
                else Color.White.copy(alpha = 0.1f)
            )
        )
        categoryInfo.forEachIndexed { index, (name, emoji) ->
            FilterChip(
                selected = selectedIndex == index,
                onClick = { onSelect(index) },
                label = { Text("$emoji $name", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = categoryColors[index].copy(alpha = 0.3f),
                    containerColor = Color.White.copy(alpha = 0.05f),
                    selectedLabelColor = Color.White,
                    labelColor = Color.White.copy(alpha = 0.7f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (selectedIndex == index) categoryColors[index].copy(alpha = 0.5f)
                    else Color.White.copy(alpha = 0.1f)
                )
            )
        }
    }
}

@Composable
private fun EventCard(
    event: Event,
    onDelete: () -> Unit
) {
    val (catEmoji, catName) = categoryForEvent(event)
    val catColor = categoryColorForEvent(event)

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = formatDay(event.dateTime),
                    color = catColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    text = formatMonth(event.dateTime),
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(catColor.copy(alpha = 0.15f))
                        .border(
                            1.dp, catColor.copy(alpha = 0.4f),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        "$catEmoji $catName",
                        color = catColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    event.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (event.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        event.description,
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Schedule,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            formatTime(event.dateTime),
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp
                        )
                    }
                    if (event.location.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "\uD83D\uDCCD",
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                event.location,
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFEF4444).copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("\uD83D\uDCC5", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No tienes eventos pr\u00F3ximos",
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Empieza creando uno nuevo para organizarte mejor",
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CreateEventDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Long, String, String, Boolean, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("personal") }
    var reminderActive by remember { mutableStateOf(false) }
    var reminderMinutesBefore by remember { mutableStateOf(30) }
    var errorMessage by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF12121A)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    "Crear Nuevo Evento",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("T\u00EDtulo del Evento") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = NeonPurple,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripci\u00F3n (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = NeonPurple,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = dateTime,
                    onValueChange = { dateTime = it },
                    label = { Text("Fecha y Hora (dd/mm/yyyy HH:mm)") },
                    placeholder = { Text("dd/mm/yyyy HH:mm", color = Color.White.copy(alpha = 0.3f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = NeonPurple,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                    )
                )

                Text(
                    "Solo se permiten fechas presentes o futuras",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Ubicaci\u00F3n (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = NeonPurple,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Categoría",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categoryInfo.size) { index ->
                        val (catName, catEmoji) = categoryInfo[index]
                        val selected = category == catName.lowercase()
                        FilterChip(
                            selected = selected,
                            onClick = { category = catName.lowercase() },
                            label = { Text("$catEmoji $catName", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = categoryColors[index].copy(alpha = 0.3f),
                                containerColor = Color.White.copy(alpha = 0.05f),
                                selectedLabelColor = Color.White,
                                labelColor = Color.White.copy(alpha = 0.7f)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (selected) categoryColors[index].copy(alpha = 0.5f)
                                else Color.White.copy(alpha = 0.1f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Recordatorio",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        checked = reminderActive,
                        onCheckedChange = { reminderActive = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NeonPurple,
                            checkedTrackColor = NeonPurple.copy(alpha = 0.5f),
                            uncheckedThumbColor = Color.White.copy(alpha = 0.5f),
                            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }

                if (reminderActive) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Minutos antes",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(5, 15, 30, 60).forEach { minutes ->
                            FilterChip(
                                selected = reminderMinutesBefore == minutes,
                                onClick = { reminderMinutesBefore = minutes },
                                label = { Text("${minutes}m", fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonPurple.copy(alpha = 0.3f),
                                    containerColor = Color.White.copy(alpha = 0.05f),
                                    selectedLabelColor = Color.White,
                                    labelColor = Color.White.copy(alpha = 0.7f)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (reminderMinutesBefore == minutes) NeonPurple.copy(alpha = 0.5f)
                                    else Color.White.copy(alpha = 0.1f)
                                )
                            )
                        }
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        errorMessage,
                        color = Color(0xFFEF4444),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color.White.copy(alpha = 0.6f))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                errorMessage = "El t\u00EDtulo es requerido"
                                return@Button
                            }
                            if (dateTime.isBlank()) {
                                errorMessage = "La fecha y hora son requeridas"
                                return@Button
                            }
                            try {
                                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                val parsedDate = sdf.parse(dateTime)
                                if (parsedDate != null) {
                                    val eventTime = parsedDate.time
                                    val currentNow = System.currentTimeMillis()
                                    if (eventTime < currentNow) {
                                        errorMessage = "La fecha debe ser presente o futura"
                                        return@Button
                                    }
                                    onConfirm(title, description, eventTime, location, category, reminderActive, reminderMinutesBefore)
                                } else {
                                    errorMessage = "Formato de fecha inv\u00E1lido"
                                }
                            } catch (e: Exception) {
                                errorMessage = "Formato de fecha inv\u00E1lido. Use dd/mm/yyyy HH:mm"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                    ) {
                        Text("Guardar Evento", color = Color.White)
                    }
                }
            }
        }
    }
}

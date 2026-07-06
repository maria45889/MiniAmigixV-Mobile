package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.GlassCard
import com.miniamigixv.miniamigixv_app.ui.theme.ThemeViewModel
import kotlinx.coroutines.delay

private val NeonPurple = Color(0xFF8B5CF6)
private val NeonBlue = Color(0xFF06B6D4)
private val NeonCyan = Color(0xFF22D3EE)
private val NeonGreen = Color(0xFF10B981)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(themeViewModel: ThemeViewModel, onBack: () -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(
        Brush.radialGradient(colors = listOf(Color(0xFF09090F), Color(0xFF0F172A)))
    )) {
        val isCompact = maxWidth < 600.dp

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Estudio", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Summary panel
                item { SummaryPanel() }

                // Quick actions row
                item { QuickActionsRow(isCompact = isCompact) }

                // Study tools grid
                item { TextSummaryCard() }

                item { QuickNotesCard() }

                if (isCompact) {
                    item { PomodoroCard() }
                    item { MiniCalendarCard() }
                } else {
                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f)) { PomodoroCard() }
                            Box(modifier = Modifier.weight(1f)) { MiniCalendarCard() }
                        }
                    }
                }

                item { StopwatchCard() }

                item { StatisticsCard() }

                item { SummaryHistoryCard() }

                item { MotivationCard() }
            }
        }
    }
}

@Composable
private fun SummaryPanel() {
    GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 24.dp) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("📚", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sesión de Estudio", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    java.text.SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", java.util.Locale("es", "ES")).format(java.util.Date()),
                    color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(emoji = "⏱", label = "Hoy", value = "0h 0m", color = NeonBlue, modifier = Modifier.weight(1f))
                StatCard(emoji = "🔥", label = "Racha", value = "0 días", color = Color(0xFFF59E0B), modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(emoji = "✅", label = "Tareas", value = "0", color = NeonGreen, modifier = Modifier.weight(1f))
                StatCard(emoji = "📄", label = "Resúmenes", value = "0", color = NeonPurple, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatCard(emoji: String, label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier, cornerRadius = 16.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}

@Composable
private fun QuickActionsRow(isCompact: Boolean) {
    val actions = listOf(
        Triple("📄", "Resumir", NeonPurple),
        Triple("📝", "Notas", NeonBlue),
        Triple("🍅", "Pomodoro", NeonGreen),
        Triple("📅", "Calendario", NeonCyan)
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(if (isCompact) 8.dp else 12.dp)
    ) {
        actions.forEach { (emoji, label, color) ->
            GlassCard(
                modifier = Modifier.weight(1f).clickable { },
                cornerRadius = 14.dp
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(emoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun TextSummaryCard() {
    var textToSummarize by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var isSummarizing by remember { mutableStateOf(false) }
    var selectedLevel by remember { mutableStateOf("medio") }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📝", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Resumen de Texto", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Pega tu texto aquí para obtener un resumen automático usando IA.", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = textToSummarize,
                onValueChange = { textToSummarize = it },
                modifier = Modifier.fillMaxWidth().height(140.dp),
                placeholder = { Text("Escribe o pega el texto que quieres resumir...", color = Color.White.copy(alpha = 0.3f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = NeonPurple
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Nivel de resumen:", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
                Spacer(modifier = Modifier.width(12.dp))
                listOf("corto" to "Corto", "medio" to "Medio", "detallado" to "Detallado").forEach { (key, label) ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                        RadioButton(
                            selected = selectedLevel == key,
                            onClick = { selectedLevel = key },
                            colors = RadioButtonDefaults.colors(selectedColor = NeonPurple, unselectedColor = Color.White.copy(alpha = 0.3f))
                        )
                        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    isSummarizing = true
                    summary = if (textToSummarize.isNotBlank()) {
                        "Resumen ($selectedLevel): ${textToSummarize.take(100)}..."
                    } else {
                        "Por favor ingresa algún texto para resumir."
                    }
                    isSummarizing = false
                },
                enabled = textToSummarize.isNotBlank() && !isSummarizing,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().background(
                        brush = Brush.linearGradient(colors = listOf(NeonPurple, NeonBlue)),
                        shape = RoundedCornerShape(24.dp)
                    ).padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSummarizing) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("🤖 Resumir con IA", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            if (summary.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 12.dp) {
                    Text(summary, color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun QuickNotesCard() {
    var newNote by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf(listOf("Repasar integrales", "Leer capítulo 4 de Biología")) }
    var selectedColor by remember { mutableStateOf(Color(0xFFFEF08A)) }

    val noteColors = listOf(
        Color(0xFFFEF08A), Color(0xFFBBF7D0),
        Color(0xFFBFDBFE), Color(0xFFFBCFE8), Color(0xFFDDD6FE)
    )

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💡", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Notas Rápidas", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                noteColors.forEach { color ->
                    Box(
                        modifier = Modifier.size(28.dp).clip(CircleShape)
                            .background(color)
                            .clickable { selectedColor = color }
                            .then(
                                if (selectedColor == color) Modifier.border(2.dp, Color.White, CircleShape)
                                else Modifier
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = newNote,
                onValueChange = { newNote = it },
                modifier = Modifier.fillMaxWidth().height(80.dp),
                placeholder = { Text("Escribe una nota rápida...", color = Color.White.copy(alpha = 0.3f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = NeonPurple
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (newNote.isNotBlank()) {
                            notes = notes + newNote
                            newNote = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().background(
                            Brush.linearGradient(colors = listOf(NeonPurple, NeonBlue)),
                            RoundedCornerShape(12.dp)
                        ).padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) { Text("💾 Guardar", color = Color.White, fontWeight = FontWeight.SemiBold) }
                }
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, NeonPurple)
                ) {
                    Text("📌 Fijar", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                notes.forEach { note ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.05f)).padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.FiberManualRecord, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(note, color = Color.White, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun PomodoroCard() {
    var isRunning by remember { mutableStateOf(false) }
    var seconds by remember { mutableStateOf(25 * 60) }
    var totalSeconds by remember { mutableStateOf(25 * 60) }
    var pomodoroEmoji by remember { mutableStateOf("🍅") }
    var selectedPreset by remember { mutableStateOf("trabajo") }

    LaunchedEffect(isRunning) {
        while (isRunning && seconds > 0) {
            delay(1000)
            seconds--
        }
        if (seconds == 0 && isRunning) {
            isRunning = false
        }
    }

    val mins = (seconds / 60).toString().padStart(2, '0')
    val secs = (seconds % 60).toString().padStart(2, '0')
    val progress = seconds.toFloat() / totalSeconds.toFloat()

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🍅", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pomodoro", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Técnica Pomodoro para sesiones de estudio enfocadas", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(contentAlignment = Alignment.Center) {
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = NeonGreen,
                        trackColor = Color.White.copy(alpha = 0.1f),
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(pomodoroEmoji, fontSize = 40.sp)
                Text("$mins:$secs", color = NeonGreen, fontSize = 42.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { isRunning = !isRunning },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    modifier = Modifier.weight(1f)
                ) { Text(if (isRunning) "⏸ Pausar" else "▶ Iniciar", color = Color.White) }
                OutlinedButton(
                    onClick = { seconds = totalSeconds; isRunning = false },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) { Text("⟳ Reiniciar", color = Color.White) }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                PomodoroPresetButton("🍅 Pomodoro (25 min)", NeonGreen) {
                    totalSeconds = 25 * 60; seconds = 25 * 60; isRunning = false; pomodoroEmoji = "🍅"; selectedPreset = "trabajo"
                }
                PomodoroPresetButton("☕ Descanso corto (5 min)", NeonBlue) {
                    totalSeconds = 5 * 60; seconds = 5 * 60; isRunning = false; pomodoroEmoji = "☕"; selectedPreset = "descanso_corto"
                }
                PomodoroPresetButton("🌴 Descanso largo (15 min)", NeonPurple) {
                    totalSeconds = 15 * 60; seconds = 15 * 60; isRunning = false; pomodoroEmoji = "🌴"; selectedPreset = "descanso_largo"
                }
            }
        }
    }
}

@Composable
private fun PomodoroPresetButton(label: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(Color.White.copy(alpha = 0.05f))
            .clickable { onClick() }.padding(12.dp)
    ) {
        Text(label, color = color, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MiniCalendarCard() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("📅", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Mini Calendario", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Haz clic en cualquier día para agregar un evento local", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Octubre 2026", color = NeonPurple, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb").forEach { day ->
                    Text(day, color = Color.White.copy(alpha = 0.4f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("12", "13", "14", "15", "16", "17", "18").forEachIndexed { index, day ->
                    Box(
                        modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
                            .background(if (index == 2) NeonPurple else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(day, color = if (index == 2) Color.White else Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun StopwatchCard() {
    var isRunning by remember { mutableStateOf(false) }
    var centiseconds by remember { mutableStateOf(0) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(10)
            centiseconds++
        }
    }

    val hrs = (centiseconds / 360000).toString().padStart(2, '0')
    val mins = ((centiseconds % 360000) / 6000).toString().padStart(2, '0')
    val secs = ((centiseconds % 6000) / 100).toString().padStart(2, '0')
    val cs = (centiseconds % 100).toString().padStart(2, '0')

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⏱", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cronómetro", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Cronómetro para medir tu tiempo de estudio", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("$hrs:$mins:$secs.$cs", color = NeonCyan, fontSize = 40.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { isRunning = !isRunning },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                    modifier = Modifier.weight(1f)
                ) { Text(if (isRunning) "⏸ Pausar" else "▶ Iniciar", color = Color.White) }
                OutlinedButton(
                    onClick = { isRunning = false; centiseconds = 0 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) { Text("🔄 Reiniciar", color = Color.White) }
            }
        }
    }
}

@Composable
private fun StatisticsCard() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📈", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Estadísticas", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatMini(emoji = "📖", label = "Tiempo", value = "0h 0m", color = NeonBlue, modifier = Modifier.weight(1f))
                StatMini(emoji = "🍅", label = "Pomodoros", value = "0", color = NeonGreen, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatMini(emoji = "🏆", label = "Mejor día", value = "0h 0m", color = NeonPurple, modifier = Modifier.weight(1f))
                StatMini(emoji = "🔥", label = "Racha", value = "0 días", color = Color(0xFFF59E0B), modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Horas estudiadas esta semana", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("Lun" to 2f, "Mar" to 3f, "Mié" to 1f, "Jue" to 4f, "Vie" to 2f, "Sáb" to 3f, "Dom" to 1f).forEach { (day, h) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier.width(24.dp).height((h * 24).dp).clip(RoundedCornerShape(4.dp))
                                .background(Brush.verticalGradient(listOf(NeonPurple, NeonBlue)))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(day, color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatMini(emoji: String, label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier, cornerRadius = 12.dp) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 22.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
                Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun SummaryHistoryCard() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📋", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Historial de Resúmenes", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))

            val historyItems = listOf(
                Triple("Resumen: Fotosíntesis", "Hace 2 horas", "📄"),
                Triple("Resumen: Revolución Francesa", "Ayer", "📄"),
                Triple("Sesión de estudio: 45 min", "Hace 3 días", "⏱"),
                Triple("Notas: Biología Cap 4", "Hace 5 días", "💡")
            )
            historyItems.forEach { (title, time, emoji) ->
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.05f)).padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(emoji, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(time, color = Color.White.copy(alpha = 0.4f), fontSize = 11.sp)
                    }
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.08f)).padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("📂 Reabrir", color = NeonCyan, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun MotivationCard() {
    val tips = listOf(
        "Estudia 25 minutos y descansa 5.",
        "Divide y vencerás: separa tareas grandes.",
        "El ambiente influye: busca un lugar tranquilo.",
        "Descansa tu mente cada 90 minutos.",
        "La constancia supera a la intensidad."
    )
    var currentTip by remember { mutableStateOf(tips.random()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(30000)
            currentTip = tips.random()
        }
    }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("💡", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Motivación", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("\"$currentTip\"", color = NeonCyan, fontSize = 14.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            }
            Box(
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(NeonPurple, NeonCyan)))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("82%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Prod.", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                }
            }
        }
    }
}

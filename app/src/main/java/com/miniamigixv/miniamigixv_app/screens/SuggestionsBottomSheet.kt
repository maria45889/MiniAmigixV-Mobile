package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionsBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("mejora") }
    var priority by remember { mutableStateOf("Media") }
    var isSending by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val categories = listOf(
        "mejora" to "🚀 Mejora de funcionalidad",
        "bug" to "🐛 Reporte de error",
        "nueva" to "✨ Nueva característica",
        "diseno" to "🎨 Diseño/UI",
        "otro" to "📌 Otro"
    )

    val categoryColors = mapOf(
        "mejora" to Color(0xFF8B5CF6),
        "bug" to Color(0xFFEF4444),
        "nueva" to Color(0xFF10B981),
        "diseno" to Color(0xFF06B6D4),
        "otro" to Color(0xFFF59E0B)
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Sugerencias", color = MaterialTheme.colorScheme.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title field
            NeonInput(
                value = title,
                onValueChange = { title = it },
                label = "Título",
                placeholder = "Resumen breve de tu sugerencia"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Message field
            NeonInput(
                value = message,
                onValueChange = { message = it },
                label = "Mensaje",
                placeholder = "¿Cómo podemos mejorar MiniAmigixV?",
                singleLine = false,
                minHeight = 120.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category selector
            Text("Categoría", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                categories.forEach { (key, label) ->
                    val selected = category == key
                    val color = categoryColors[key] ?: Color(0xFF8B5CF6)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (selected) color.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                            .clickable { category = key }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            label,
                            color = if (selected) color else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (selected) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Priority selector
            Text("Prioridad", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Baja", "Media", "Alta").forEach { p ->
                    NeonChip(
                        text = p,
                        selected = priority == p,
                        onClick = { priority = p }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit button
            NeonButton(
                text = if (isSending) "Enviando..." else "Enviar al Admin",
                onClick = {
                    if (title.isNotBlank() && message.isNotBlank()) {
                        isSending = true
                        // Simulate sending to backend
                        coroutineScope.launch {
                            kotlinx.coroutines.delay(2000)
                            isSending = false
                            showSuccess = true
                            kotlinx.coroutines.delay(1500)
                            onDismiss()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                icon = { 
                    if (isSending) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.background,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = MaterialTheme.colorScheme.background, modifier = Modifier.size(18.dp))
                    }
                }
            )

            if (showSuccess) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "✓ Sugerencia enviada correctamente",
                    color = Color(0xFF10B981),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

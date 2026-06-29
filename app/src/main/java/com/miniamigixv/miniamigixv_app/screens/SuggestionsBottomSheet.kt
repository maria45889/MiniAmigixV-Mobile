package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionsBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Media") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CardBg,
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
                Text("Sugerencias", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
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

            // Priority selector
            Text("Prioridad", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                text = "Enviar al Admin",
                onClick = { onDismiss() },
                modifier = Modifier.fillMaxWidth(),
                icon = { Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = BgDark, modifier = Modifier.size(18.dp)) }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

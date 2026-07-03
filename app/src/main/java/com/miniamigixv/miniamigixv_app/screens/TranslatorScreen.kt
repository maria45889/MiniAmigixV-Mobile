package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.miniamigixv.miniamigixv_app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreen(onBack: () -> Unit = {}) {
    var textInput by remember { mutableStateOf("") }
    var textOutput by remember { mutableStateOf("") }
    var isTranslating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        val isCompact = maxWidth < 600.dp

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Traductor", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    NeonHeader(title = "TRADUCTOR", subtitle = "MINIAMIGIXV")
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    NeonBorderCard {
                        // Selector de idiomas
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LanguageDropdown("Español")
                            IconButton(onClick = { /* Swap */ }, modifier = Modifier.padding(horizontal = 16.dp)) {
                                Icon(Icons.Filled.SwapHoriz, contentDescription = "Intercambiar", tint = NeonCyan)
                            }
                            LanguageDropdown("Inglés")
                        }
                        
                        Divider(color = Color.White.copy(alpha = 0.1f))
                        Spacer(modifier = Modifier.height(16.dp))

                        if (isCompact) {
                            // Mobile Layout
                            TranslatorInputBox("TEXTO ORIGINAL", textInput, "Escribe o pega texto...") { textInput = it }
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            TranslateButton(isTranslating) {
                                if (textInput.isNotBlank()) {
                                    isTranslating = true
                                    coroutineScope.launch {
                                        delay(1500)
                                        // Simple translation simulation
                                        val translations = mapOf(
                                            "hola" to "hello",
                                            "mundo" to "world",
                                            "buenos días" to "good morning",
                                            "gracias" to "thank you",
                                            "adiós" to "goodbye",
                                            "por favor" to "please",
                                            "amor" to "love",
                                            "amigo" to "friend",
                                            "feliz" to "happy",
                                            "triste" to "sad"
                                        )
                                        val lowerInput = textInput.lowercase().trim()
                                        textOutput = translations[lowerInput] ?: translateFallback(textInput)
                                        isTranslating = false
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            TranslatorOutputBox("TRADUCCIÓN", textOutput, "La traducción aparecerá aquí...")
                        } else {
                            // Desktop Layout
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Box(modifier = Modifier.weight(1f)) {
                                    TranslatorInputBox("TEXTO ORIGINAL", textInput, "Escribe o pega texto...") { textInput = it }
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    TranslatorOutputBox("TRADUCCIÓN", textOutput, "La traducción aparecerá aquí...")
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            TranslateButton(isTranslating) {
                                if (textInput.isNotBlank()) {
                                    isTranslating = true
                                    coroutineScope.launch {
                                        delay(1500)
                                        // Simple translation simulation
                                        val translations = mapOf(
                                            "hola" to "hello",
                                            "mundo" to "world",
                                            "buenos días" to "good morning",
                                            "gracias" to "thank you",
                                            "adiós" to "goodbye",
                                            "por favor" to "please",
                                            "amor" to "love",
                                            "amigo" to "friend",
                                            "feliz" to "happy",
                                            "triste" to "sad"
                                        )
                                        val lowerInput = textInput.lowercase().trim()
                                        textOutput = translations[lowerInput] ?: translateFallback(textInput)
                                        isTranslating = false
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        StatusBox(Modifier.weight(1f), "STATUS", "ACTIVE", NeonPurple)
                        StatusBox(Modifier.weight(1f), "IDIOMA DETECTADO", "ESPAÑOL", NeonCyan)
                    }
                }

                item {
                    HistorySection()
                }
            }
        }
    }
}

@Composable
private fun LanguageDropdown(selected: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(selected, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun TranslatorInputBox(title: String, text: String, placeholder: String, onValueChange: (String) -> Unit) {
    Column {
        Text(title, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(150.dp),
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

@Composable
private fun TranslatorOutputBox(title: String, text: String, placeholder: String) {
    Column {
        Text(title, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = text,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth().height(150.dp),
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

@Composable
private fun TranslateButton(isTranslating: Boolean, onClick: () -> Unit) {
    NeonButton(
        text = if (isTranslating) "TRADUCIENDO..." else "INICIAR TRADUCCIÓN NEURAL",
        onClick = onClick,
        icon = {
            if (isTranslating) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.background, strokeWidth = 2.dp)
            } else {
                Icon(Icons.Filled.Bolt, contentDescription = null, tint = MaterialTheme.colorScheme.background)
            }
        }
    )
}

@Composable
private fun StatusBox(modifier: Modifier, label: String, value: String, color: Color) {
    NeonCard(modifier = modifier) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.FiberManualRecord, contentDescription = null, tint = color, modifier = Modifier.size(10.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
private fun HistorySection() {
    NeonCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.History, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("REGISTRO DE TRADUCCIONES", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HistoryItem("Quito", "Quito (Translated)")
            HistoryItem("hola", "hello")
        }
    }
}

@Composable
private fun HistoryItem(original: String, translated: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .padding(12.dp)
    ) {
        Text("[ESPAÑOL -> INGLÉS]", color = NeonPurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(original, color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(translated, color = NeonCyan, fontSize = 14.sp)
    }
}

private fun translateFallback(text: String): String {
    // Fallback translation for words not in dictionary
    return text.split(" ").joinToString(" ") { word ->
        when (word.lowercase()) {
            "el", "la", "los", "las" -> "the"
            "de" -> "of"
            "en" -> "in"
            "y" -> "and"
            "que" -> "that"
            "por" -> "for"
            "con" -> "with"
            "un", "una" -> "a"
            "es" -> "is"
            "son" -> "are"
            else -> word // Keep original if no translation
        }
    }
}

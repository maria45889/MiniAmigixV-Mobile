package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.GlassCard
import com.miniamigixv.miniamigixv_app.ui.theme.ThemeViewModel
import kotlinx.coroutines.launch

private val NeonPurple = Color(0xFF8B5CF6)
private val NeonBlue = Color(0xFF06B6D4)
private val NeonCyan = Color(0xFF22D3EE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    themeViewModel: ThemeViewModel,
    onBack: () -> Unit = {}
) {
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
                        "Soporte",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    HeroSection()
                }

                item {
                    SectionHeader("Preguntas Frecuentes")
                }
                item {
                    GlassCard {
                        FaqItem(
                            "Como creo mi cuenta?",
                            "Para crear tu cuenta, ve a la seccion de Inicio y presiona 'Registrarse'. Llena tus datos y estaras listo."
                        )
                    }
                }
                item {
                    GlassCard {
                        FaqItem(
                            "La plataforma es gratuita?",
                            "Si, el acceso a MiniAmigixV es gratuito con opciones premium adicionales."
                        )
                    }
                }
                item {
                    GlassCard {
                        FaqItem(
                            "Como uso el Chat IA?",
                            "Solo ingresa al modulo Chat IA, escribe tu pregunta en la caja inferior y el asistente te respondera instantaneamente."
                        )
                    }
                }
                item {
                    GlassCard {
                        FaqItem(
                            "Como reporto un error?",
                            "Puedes usar el formulario a continuacion para describir el error que encontraste."
                        )
                    }
                }

                item {
                    SectionHeader("Contacto")
                }
                item {
                    GlassCard {
                        ContactRow(
                            icon = Icons.Filled.Email,
                            label = "Email",
                            value = "miniamigixv@gmail.com"
                        )
                        GlassDivider()
                        ContactRow(
                            icon = Icons.Filled.Chat,
                            label = "WhatsApp",
                            value = "+52 55 1234 5678"
                        )
                        GlassDivider()
                        ContactRow(
                            icon = Icons.Filled.Description,
                            label = "Formulario",
                            value = "Enviar reporte"
                        )
                    }
                }

                item {
                    SectionHeader("Reportar un problema")
                }
                item {
                    ReportProblemCard()
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun HeroSection() {
    GlassCard(cornerRadius = 24.dp) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "\uD83C\uDF9F\uFE0F",
                fontSize = 48.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Como podemos ayudarte?",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Estamos aqui para resolver tus dudas",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun FaqItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                question,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f)
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.08f))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    answer,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun ContactRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = NeonBlue,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                label,
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                value,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ReportProblemCard() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    var sent by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    GlassCard {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "Describe tu problema",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                "Nuestro equipo te ayudara personalmente.",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp
            )

            InputField(value = name, label = "Nombre", placeholder = "Tu nombre", onValueChange = { name = it })
            InputField(value = email, label = "Email", placeholder = "tu@email.com", onValueChange = { email = it })
            InputField(value = message, label = "Mensaje", placeholder = "Describe tu problema...", onValueChange = { message = it })

            Button(
                onClick = {
                    if (name.isNotBlank() && email.isNotBlank() && message.isNotBlank()) {
                        isSending = true
                        scope.launch {
                            kotlinx.coroutines.delay(2000)
                            isSending = false
                            sent = true
                            name = ""
                            email = ""
                            message = ""
                        }
                    }
                },
                enabled = !isSending && name.isNotBlank() && email.isNotBlank() && message.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonPurple
                )
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Enviar Reporte", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            if (sent) {
                Text(
                    "Reporte enviado correctamente",
                    color = Color(0xFF10B981),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun InputField(
    value: String,
    label: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            label,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    placeholder,
                    color = Color.White.copy(alpha = 0.3f)
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonPurple,
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.White.copy(alpha = 0.05f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        title,
        color = NeonPurple,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun GlassDivider() {
    Spacer(modifier = Modifier.height(8.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.05f))
    )
    Spacer(modifier = Modifier.height(8.dp))
}

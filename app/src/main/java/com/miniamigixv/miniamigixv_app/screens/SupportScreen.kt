package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(onBack: () -> Unit = {}) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(BgDark)) {
        val isCompact = maxWidth < 600.dp

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Centro de Soporte", fontWeight = FontWeight.Bold, color = TextWhite) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBg)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    NeonHeader(
                        title = "¿Cómo podemos ayudarte?", 
                        subtitle = "Estamos aquí para resolver tus dudas y hacer tu experiencia increíble.",
                        icon = { Icon(Icons.Filled.SupportAgent, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(48.dp)) }
                    )
                }

                item {
                    if (isCompact) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            InfoCards()
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            InfoCards(Modifier.weight(1f))
                        }
                    }
                }

                item {
                    Text("Preguntas Frecuentes", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        FaqItem("¿Cómo creo mi cuenta?", "Para crear tu cuenta, ve a la sección de Inicio y presiona 'Registrarse'. Llena tus datos y estarás listo.")
                        FaqItem("¿La plataforma es gratuita?", "Sí, el acceso a MiniAmigixV es gratuito con opciones premium adicionales.")
                        FaqItem("¿Cómo uso el Chat IA?", "Solo ingresa al módulo Chat IA, escribe tu pregunta en la caja inferior y el asistente te responderá instantáneamente.")
                        FaqItem("¿Cómo reporto un error?", "Puedes usar el formulario a continuación para describir el error que encontraste.")
                    }
                }

                item {
                    ContactForm()
                }
            }
        }
    }
}

@Composable
private fun InfoCards(modifier: Modifier = Modifier) {
    InfoCard(modifier, "Qué es MiniAmigixV", "Una plataforma todo en uno con Chat IA, música, juegos y mucho más. Diseñada para simplificar tu vida digital.", Icons.Filled.Info)
    InfoCard(modifier, "Misión", "Crear una app moderna, inteligente y accesible que unifique herramientas útiles en un solo lugar.", Icons.Filled.TrackChanges)
    InfoCard(modifier, "Visión", "Ser una plataforma global tipo 'super app' educativa y social que transforme la manera en que las personas interactúan con la tecnología.", Icons.Filled.Visibility)
}

@Composable
private fun InfoCard(modifier: Modifier, title: String, text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    NeonCard(modifier = modifier) {
        Icon(icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text, color = TextGray, fontSize = 12.sp)
    }
}

@Composable
private fun FaqItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    NeonCard(padding = 0.dp) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(question, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Icon(
                    if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = TextGray
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    Divider(color = Color.White.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(answer, color = TextGray, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun ContactForm() {
    Text("Escríbenos", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    Text("¿No encontraste la respuesta? Nuestro equipo te ayudará personalmente.", color = TextGray, fontSize = 14.sp)
    Spacer(modifier = Modifier.height(16.dp))

    NeonBorderCard {
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var subject by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }

        CustomOutlinedField(name, "Tu nombre", "Ej: Juan Pérez") { name = it }
        Spacer(modifier = Modifier.height(12.dp))
        CustomOutlinedField(email, "Tu correo", "ejemplo@correo.com") { email = it }
        Spacer(modifier = Modifier.height(12.dp))
        CustomOutlinedField(subject, "Asunto", "Selecciona un asunto") { subject = it }
        Spacer(modifier = Modifier.height(12.dp))
        
        Text("Mensaje", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            placeholder = { Text("Describe tu consulta con detalle...", color = TextGray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonPurple,
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedContainerColor = BgDark,
                unfocusedContainerColor = BgDark
            ),
            shape = RoundedCornerShape(8.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        NeonButton(
            text = "Enviar mensaje",
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            icon = { Icon(Icons.Filled.Send, contentDescription = null, tint = BgDark, modifier = Modifier.size(18.dp)) }
        )
    }
}

@Composable
private fun CustomOutlinedField(value: String, label: String, placeholder: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = TextGray) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonPurple,
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedContainerColor = BgDark,
                unfocusedContainerColor = BgDark
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

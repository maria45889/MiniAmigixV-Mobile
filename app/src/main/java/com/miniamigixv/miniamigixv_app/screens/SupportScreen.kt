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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(onBack: () -> Unit = {}) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        val isCompact = maxWidth < 600.dp

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Centro de Soporte", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
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
                    Text("Preguntas Frecuentes", color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
        Text(title, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
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
                Text(question, color = MaterialTheme.colorScheme.onBackground, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Icon(
                    if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    Divider(color = Color.White.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(answer, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun ContactForm() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(NeonPurple, Color(0xFF3B82F6))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Escríbenos",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "¿No encontraste la respuesta? Nuestro equipo te ayudará personalmente.",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            fontSize = 14.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Form fields
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomOutlinedField(name, "Nombre", "Tu nombre", { name = it })
            CustomOutlinedField(email, "Email", "tu@email.com", { email = it })
            CustomOutlinedField(message, "Mensaje", "Describe tu problema...", { message = it })
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank() && message.isNotBlank()) {
                    isSending = true
                    // Simulate sending email
                    coroutineScope.launch {
                        kotlinx.coroutines.delay(2000)
                        isSending = false
                        showMessage = true
                        name = ""
                        email = ""
                        message = ""
                    }
                }
            },
            enabled = !isSending && name.isNotBlank() && email.isNotBlank() && message.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            if (isSending) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = NeonPurple,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Enviar Mensaje", color = NeonPurple, fontWeight = FontWeight.Bold)
            }
        }

        if (showMessage) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "✓ Mensaje enviado correctamente",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Contact Info Cards
        ContactInfoCard(
            icon = Icons.Filled.Email,
            label = "Email",
            value = "miniamigixv@gmail.com"
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ContactInfoCard(
            icon = Icons.Filled.AccessTime,
            label = "Tiempo de respuesta",
            value = "Menos de 24 horas"
        )
    }
}

@Composable
private fun ContactInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                label,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CustomOutlinedField(value: String, label: String, placeholder: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonPurple,
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

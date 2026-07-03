package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miniamigixv.miniamigixv_app.ui.components.*
import com.miniamigixv.miniamigixv_app.ui.theme.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit = {},
    onSave: (username: String, bio: String, birthdate: String, theme: String, language: String) -> Unit = { _, _, _, _, _ -> },
    themeViewModel: ThemeViewModel = viewModel()
) {
    var username by remember { mutableStateOf("maria45889") }
    var bio by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("11/05/2005") }
    
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    var theme by remember { mutableStateOf(if (isDarkTheme) "Oscuro" else "Claro") }
    var language by remember { mutableStateOf("Español") }

    val themeOptions = listOf("Oscuro", "Claro")
    val languageOptions = listOf("Español", "English", "Português", "Français")

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).systemBarsPadding()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Editar Perfil", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Avatar Section
                NeonBorderCard(padding = 24.dp) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 3.dp,
                                    brush = Brush.linearGradient(listOf(NeonPurple, NeonCyan)),
                                    shape = CircleShape
                                )
                                .background(NeonPurple.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(50.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { 
                                // Image picker would be implemented here
                                // For now, show a toast message
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Filled.CameraAlt, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cambiar Avatar", color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Username
                NeonInput(
                    value = username,
                    onValueChange = { username = it },
                    label = "Nombre de Usuario",
                    placeholder = "Ingresa tu nombre de usuario"
                )

                // Biography
                NeonInput(
                    value = bio,
                    onValueChange = { bio = it },
                    label = "Biografía",
                    placeholder = "Cuéntanos sobre ti...",
                    singleLine = false,
                    minHeight = 120.dp
                )

                // Birthdate
                NeonInput(
                    value = birthdate,
                    onValueChange = { birthdate = it },
                    label = "Fecha de Nacimiento",
                    placeholder = "DD/MM/AAAA"
                )

                // Theme Selection
                NeonCard {
                    Column {
                        Text("Tema", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            themeOptions.forEach { option ->
                                NeonChip(
                                    text = option,
                                    selected = theme == option,
                                    onClick = { 
                                        theme = option
                                        // Update theme immediately for preview
                                        themeViewModel.setTheme(option == "Oscuro")
                                    },
                                    icon = if (option == "Oscuro") Icons.Filled.DarkMode else Icons.Filled.LightMode
                                )
                            }
                        }
                    }
                }

                // Language Selection
                NeonCard {
                    Column {
                        Text("Idioma", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            languageOptions.forEach { option ->
                                NeonChip(
                                    text = option,
                                    selected = language == option,
                                    onClick = { language = option },
                                    icon = Icons.Filled.Language
                                )
                            }
                        }
                    }
                }

                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onSave(username, bio, birthdate, theme, language) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Filled.Save, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar Cambios", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

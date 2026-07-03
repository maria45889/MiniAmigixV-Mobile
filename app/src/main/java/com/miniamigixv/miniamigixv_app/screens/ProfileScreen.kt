package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miniamigixv.miniamigixv_app.auth.AuthViewModel
import com.miniamigixv.miniamigixv_app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit = {},
    onNavigateToEdit: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).systemBarsPadding()) {
        val isCompact = maxWidth < 600.dp

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Perfil", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Profile Header Card
                item {
                    NeonBorderCard(padding = 24.dp) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 2.dp,
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
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text("Majito", color = MaterialTheme.colorScheme.onBackground, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Sin biografía. ¡Cuéntanos de ti!", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                            }

                            // Edit button
                            Button(
                                onClick = onNavigateToEdit,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Filled.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Editar", color = MaterialTheme.colorScheme.onPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Chips with FlowRow
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            NeonChip(text = "Jun 2026", icon = Icons.Filled.CalendarMonth)
                            NeonChip(text = "Oscuro", icon = Icons.Filled.DarkMode)
                            NeonChip(text = "ES", icon = Icons.Filled.Language)
                        }
                    }
                }

                // Metrics
                item {
                    if (isCompact) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            NeonMetricCard(icon = Icons.Filled.Event, iconColor = NeonPurple, value = "0", label = "Eventos Creados")
                            NeonMetricCard(icon = Icons.Filled.NotificationsActive, iconColor = Color(0xFFEC4899), value = "62", label = "Alertas")
                            NeonMetricCard(icon = Icons.Filled.TrendingUp, iconColor = Color(0xFF10B981), value = "18", label = "Días Activo")
                        }
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            NeonMetricCard(modifier = Modifier.weight(1f), icon = Icons.Filled.Event, iconColor = NeonPurple, value = "0", label = "Eventos Creados")
                            NeonMetricCard(modifier = Modifier.weight(1f), icon = Icons.Filled.NotificationsActive, iconColor = Color(0xFFEC4899), value = "62", label = "Alertas")
                            NeonMetricCard(modifier = Modifier.weight(1f), icon = Icons.Filled.TrendingUp, iconColor = Color(0xFF10B981), value = "18", label = "Días Activo")
                        }
                    }
                }

                // Bio hint
                item {
                    NeonCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Personaliza tu perfil añadiendo una biografía en ",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Logout
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = {
                            authViewModel.logout()
                            onBack()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                        border = BorderStroke(1.dp, Color(0xFFEF4444))
                    ) {
                        Icon(Icons.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar sesión", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

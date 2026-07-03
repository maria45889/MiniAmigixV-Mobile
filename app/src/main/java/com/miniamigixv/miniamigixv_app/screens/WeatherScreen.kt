package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miniamigixv.miniamigixv_app.data.model.WeatherData
import com.miniamigixv.miniamigixv_app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    onBack: () -> Unit = {},
    weatherViewModel: WeatherViewModel = viewModel()
) {
    val state = weatherViewModel.uiState
    val query = weatherViewModel.searchQuery
    val focusManager = LocalFocusManager.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).systemBarsPadding()) {
        val isCompact = maxWidth < 600.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(
                title = { Text("CLIMA", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Buscador
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = weatherViewModel::updateSearchQuery,
                        modifier = Modifier.weight(1f).height(56.dp),
                        placeholder = { Text("Buscar ciudad...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            focusManager.clearFocus()
                            weatherViewModel.searchWeather()
                        })
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            weatherViewModel.searchWeather()
                        },
                        modifier = Modifier.height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                    ) {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = MaterialTheme.colorScheme.background)
                    }
                }

                // Ciudades populares
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Bogotá", "Medellín", "Cali", "Quito").forEach { city ->
                        OutlinedButton(
                            onClick = {
                                weatherViewModel.updateSearchQuery(city)
                                weatherViewModel.searchWeather()
                            },
                            modifier = Modifier.height(36.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Text(city, fontSize = 12.sp)
                        }
                    }
                }

                when (val s = state) {
                    is WeatherUiState.Idle -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Busca una ciudad para ver el clima", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    is WeatherUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = NeonCyan)
                        }
                    }
                    is WeatherUiState.Error -> {
                        NeonCard {
                            Text(s.message, color = MaterialTheme.colorScheme.error)
                        }
                    }
                    is WeatherUiState.Success -> {
                        if (isCompact) {
                            // Mobile Order: Main -> Details -> Radar -> Pronóstico
                            MainWeatherCard(s.data)
                            DetailsRow(s.data)
                            RadarCard()
                            ForecastCard()
                        } else {
                            // Desktop Order
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                    MainWeatherCard(s.data)
                                }
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                    RadarCard()
                                    DetailsRow(s.data)
                                    ForecastCard()
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun MainWeatherCard(data: WeatherData) {
    NeonCard(padding = 32.dp) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Filled.Cloud, // Or map WeatherIconMapper
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = NeonCyan
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "${data.temperature.toInt()}°C",
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                data.city.uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 2.sp
            )
            Text(
                data.description.uppercase(),
                fontSize = 14.sp,
                color = NeonPurple,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun DetailsRow(data: WeatherData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DetailBox(Modifier.weight(1f), "Humedad", "${data.humidity}%", Icons.Filled.WaterDrop)
        DetailBox(Modifier.weight(1f), "Viento", "${data.windSpeed} km/h", Icons.Filled.Air)
    }
}

@Composable
private fun DetailBox(modifier: Modifier, label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Column {
            Icon(icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
    }
}

@Composable
private fun RadarCard() {
    NeonCard {
        Text("RADAR MINIAMIGIXV", color = NeonPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        
        val infiniteTransition = rememberInfiniteTransition()
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(180.dp)) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2
                
                // Outer ring
                drawCircle(
                    color = NeonPurple.copy(alpha = 0.3f),
                    radius = radius,
                    style = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
                )
                // Inner ring
                drawCircle(
                    color = NeonCyan.copy(alpha = 0.2f),
                    radius = radius * 0.6f,
                    style = Stroke(width = 1.dp.toPx())
                )
                
                // Radar sweep
                rotate(rotation) {
                    drawArc(
                        color = NeonCyan.copy(alpha = 0.4f),
                        startAngle = 0f,
                        sweepAngle = 60f,
                        useCenter = true
                    )
                }
                
                // Dots (anomalies)
                drawCircle(color = NeonPurple, radius = 4.dp.toPx(), center = Offset(center.x - 40f, center.y + 30f))
                drawCircle(color = NeonCyan, radius = 3.dp.toPx(), center = Offset(center.x + 50f, center.y - 20f))
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("ANOMALÍA DETECTADA", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
            Text("0.04%", color = NeonCyan, fontSize = 10.sp)
        }
    }
}

@Composable
private fun ForecastCard() {
    NeonCard {
        Text("PRONÓSTICO", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ForecastItem("MAÑANA", Icons.Filled.Cloud, "20°C")
            ForecastItem("PRÓXIMO", Icons.Filled.CloudQueue, "20°C")
            ForecastItem("FIN SEM.", Icons.Filled.Cloud, "18°C")
        }
    }
}

@Composable
private fun ForecastItem(day: String, icon: androidx.compose.ui.graphics.vector.ImageVector, temp: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(day, color = MaterialTheme.colorScheme.onBackground, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(temp, color = MaterialTheme.colorScheme.onBackground, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

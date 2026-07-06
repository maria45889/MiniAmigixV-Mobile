package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miniamigixv.miniamigixv_app.data.model.WeatherData
import com.miniamigixv.miniamigixv_app.ui.components.GlassCard
import com.miniamigixv.miniamigixv_app.ui.theme.ThemeViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val NeonPurple = Color(0xFF8B5CF6)
private val NeonBlue = Color(0xFF06B6D4)
private val NeonCyan = Color(0xFF22D3EE)
private val NeonGreen = Color(0xFF10B981)
private val NeonOrange = Color(0xFFF59E0B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    themeViewModel: ThemeViewModel,
    onBack: () -> Unit,
    weatherViewModel: WeatherViewModel = viewModel()
) {
    val state = weatherViewModel.uiState
    val query = weatherViewModel.searchQuery
    val focusManager = LocalFocusManager.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF09090F), Color(0xFF0F172A))
                )
            )
    ) {
        val isCompact = maxWidth < 600.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(
                title = {
                    Text(
                        "CLIMA",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 4.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = weatherViewModel::updateSearchQuery,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        placeholder = {
                            Text(
                                "ESCANEAR CIUDAD",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 12.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            cursorColor = NeonCyan,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF12121A).copy(alpha = 0.6f),
                            unfocusedContainerColor = Color(0xFF12121A).copy(alpha = 0.4f)
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
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                    ) {
                        Text(
                            "BUSCAR",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 2.sp
                        )
                    }
                }

                when (val s = state) {
                    is WeatherUiState.Idle -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Busca una ciudad para ver el clima",
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        }
                    }
                    is WeatherUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = NeonCyan)
                        }
                    }
                    is WeatherUiState.Error -> {
                        GlassCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                s.message,
                                color = Color(0xFFEF4444)
                            )
                        }
                    }
                    is WeatherUiState.Success -> {
                        if (isCompact) {
                            MainWeatherPanel(s.data)
                            RadarPanel()
                            SideDataPanel(s.data)
                            ForecastPanel()
                            DataSourcePanel()
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    MainWeatherPanel(s.data)
                                }
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    RadarPanel()
                                }
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    SideDataPanel(s.data)
                                    ForecastPanel()
                                    DataSourcePanel()
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
private fun MainWeatherPanel(data: WeatherData) {
    var currentTime by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            delay(1000)
        }
    }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                currentTime,
                fontSize = 14.sp,
                color = NeonPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            val emoji = weatherEmoji(data.description)
            Text(
                emoji,
                fontSize = 64.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            val infiniteTransition = rememberInfiniteTransition()
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { index ->
                    val height by infiniteTransition.animateFloat(
                        initialValue = 4f,
                        targetValue = 16f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, delayMillis = index * 100),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(height.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(NeonCyan.copy(alpha = 0.6f))
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "${data.temperature.toInt()}°",
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                data.city.uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Text(
                data.description,
                fontSize = 14.sp,
                color = NeonCyan,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun RadarPanel() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                "RADAR MiniAmigixV",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonPurple,
                letterSpacing = 2.sp
            )

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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(180.dp)) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.width / 2

                    drawCircle(
                        color = NeonPurple.copy(alpha = 0.3f),
                        radius = radius,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                        )
                    )

                    drawCircle(
                        color = NeonCyan.copy(alpha = 0.2f),
                        radius = radius * 0.6f,
                        style = Stroke(width = 1.dp.toPx())
                    )

                    drawLine(
                        color = NeonPurple.copy(alpha = 0.2f),
                        start = Offset(center.x - radius * 0.7f, center.y),
                        end = Offset(center.x + radius * 0.7f, center.y),
                        strokeWidth = 1.dp.toPx()
                    )
                    drawLine(
                        color = NeonPurple.copy(alpha = 0.2f),
                        start = Offset(center.x, center.y - radius * 0.7f),
                        end = Offset(center.x, center.y + radius * 0.7f),
                        strokeWidth = 1.dp.toPx()
                    )

                    rotate(rotation) {
                        drawArc(
                            color = NeonCyan.copy(alpha = 0.4f),
                            startAngle = 0f,
                            sweepAngle = 60f,
                            useCenter = true
                        )
                    }

                    drawCircle(
                        color = NeonPurple,
                        radius = 4.dp.toPx(),
                        center = Offset(center.x - 40f, center.y + 30f)
                    )
                    drawCircle(
                        color = NeonCyan,
                        radius = 3.dp.toPx(),
                        center = Offset(center.x + 50f, center.y - 20f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "ANOMALÍA DETECTADA",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
                Text(
                    "0.04%",
                    fontSize = 10.sp,
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.04f)
                        .clip(RoundedCornerShape(2.dp))
                        .background(NeonOrange)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(NeonGreen)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "INFORME DE ANÁLISIS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.7f),
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Utilizando tecnología de espectro orbital, el Radar MiniAmigixV escanea micro-variaciones en la ionósfera para predecir cambios climáticos con precisión cuántica.",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.5f),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun SideDataPanel(data: WeatherData) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "HUMEDAD",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.7f),
                        letterSpacing = 2.sp
                    )
                    Text(
                        "${data.humidity}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(data.humidity / 100f)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(NeonBlue, NeonCyan)
                                )
                            )
                    )
                }
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "VIENTO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.7f),
                        letterSpacing = 2.sp
                    )
                    Text(
                        "${data.windSpeed} km/h",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth((data.windSpeed.toFloat() / 100f).coerceIn(0f, 1f))
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(NeonGreen, NeonGreen.copy(alpha = 0.5f))
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun ForecastPanel() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            "PRONÓSTICO",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.7f),
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ForecastItem("MAÑANA", "☀️", "20°", "12°")
            ForecastItem("PRÓXIMO", "🌧️", "14°", "8°")
            ForecastItem("FIN SEM.", "☀️", "22°", "15°")
        }
    }
}

@Composable
private fun ForecastItem(day: String, icon: String, tempMax: String, tempMin: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            day,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(icon, fontSize = 28.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            tempMax,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            tempMin,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun DataSourcePanel() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(NeonGreen)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "FUENTE DE DATOS",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 2.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Open-Meteo API: Datos meteorológicos en tiempo real. La precisión puede variar según la ubicación.",
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.5f),
            lineHeight = 18.sp
        )
    }
}

private fun weatherEmoji(description: String): String {
    val desc = description.lowercase()
    return when {
        "clear" in desc || "soleado" in desc -> "☀️"
        "cloud" in desc || "nubla" in desc -> "☁️"
        "rain" in desc || "lluvia" in desc -> "🌧️"
        else -> "⛈️"
    }
}

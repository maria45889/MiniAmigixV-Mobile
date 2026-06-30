package com.miniamigixv.miniamigixv_app.games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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

private val NeonGreen = Color(0xFF10B981)
private val NeonPurple = Color(0xFF8B5CF6)

data class Position(val x: Int, val y: Int)

@Composable
fun SnakeNeoScreen(
    onBack: () -> Unit = {}
) {
    var snake by remember { mutableStateOf(listOf(Position(5, 5))) }
    var food by remember { mutableStateOf(Position(10, 10)) }
    var direction by remember { mutableStateOf(Position(1, 0)) }
    var score by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var gameStarted by remember { mutableStateOf(false) }

    val gridSize = 20
    val cellSize = 16.dp

    fun resetGame() {
        snake = listOf(Position(5, 5))
        food = Position(10, 10)
        direction = Position(1, 0)
        score = 0
        gameOver = false
        gameStarted = true
    }

    LaunchedEffect(gameStarted) {
        if (gameStarted && !gameOver) {
            while (!gameOver) {
                delay(150)
                val newHead = Position(
                    (snake.first().x + direction.x + gridSize) % gridSize,
                    (snake.first().y + direction.y + gridSize) % gridSize
                )

                if (snake.contains(newHead)) {
                    gameOver = true
                    break
                }

                snake = if (newHead == food) {
                    score += 10
                    food = Position(
                        (0 until gridSize).random(),
                        (0 until gridSize).random()
                    )
                    snake + newHead
                } else {
                    (snake + newHead).dropLast(1)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Close button
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = MaterialTheme.colorScheme.onBackground)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                "Snake Neo",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Usa las flechas para mover la serpiente",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Score
            Text(
                "Puntuación: $score",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NeonGreen
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Game Board
            Box(
                modifier = Modifier
                    .size((gridSize * cellSize.value).dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(2.dp, NeonPurple, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
            ) {
                // Draw grid cells
                for (y in 0 until gridSize) {
                    for (x in 0 until gridSize) {
                        val isSnakeHead = snake.firstOrNull() == Position(x, y)
                        val isSnakeBody = snake.contains(Position(x, y))
                        val isFood = food == Position(x, y)

                        Box(
                            modifier = Modifier
                                .offset(x = cellSize * x, y = cellSize * y)
                                .size(cellSize)
                                .background(
                                    when {
                                        isSnakeHead -> NeonGreen
                                        isSnakeBody -> NeonGreen.copy(alpha = 0.6f)
                                        isFood -> Color(0xFFEF4444)
                                        else -> Color.Transparent
                                    }
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Control buttons
            if (!gameStarted) {
                Button(
                    onClick = { resetGame() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonGreen
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        "Iniciar Juego",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else {
                // Direction controls
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { direction = Position(0, -1) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonPurple
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(60.dp)
                    ) {
                        Text("↑", fontSize = 24.sp, color = Color.White)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { direction = Position(-1, 0) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonPurple
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(60.dp)
                        ) {
                            Text("←", fontSize = 24.sp, color = Color.White)
                        }
                        Button(
                            onClick = { direction = Position(1, 0) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonPurple
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(60.dp)
                        ) {
                            Text("→", fontSize = 24.sp, color = Color.White)
                        }
                    }
                    Button(
                        onClick = { direction = Position(0, 1) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonPurple
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(60.dp)
                    ) {
                        Text("↓", fontSize = 24.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Restart button
            Button(
                onClick = { resetGame() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    "Reiniciar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Game Over message
            if (gameOver) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEF4444).copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "¡Game Over!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Puntuación final: $score",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

private val NeonBlue = Color(0xFF3B82F6)
private val NeonPink = Color(0xFFEC4899)

class TicTacToeViewModel : ViewModel() {
    val board = mutableStateListOf<Char?>(*Array(9) { null })
    var currentPlayer by mutableStateOf('X')
    var winner by mutableStateOf<Char?>(null)
    var isGameOver by mutableStateOf(false)

    fun makeMove(index: Int) {
        if (board[index] == null && !isGameOver) {
            board[index] = currentPlayer
            if (checkWinner()) {
                winner = currentPlayer
                isGameOver = true
            } else if (board.all { it != null }) {
                isGameOver = true
            } else {
                currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
                if (currentPlayer == 'O' && !isGameOver) {
                    makeCpuMove()
                }
            }
        }
    }

    private fun makeCpuMove() {
        val availableMoves = board.indices.filter { board[it] == null }
        if (availableMoves.isNotEmpty()) {
            val cpuMove = availableMoves.random()
            makeMove(cpuMove)
        }
    }

    private fun checkWinner(): Boolean {
        val winPatterns = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        for (pattern in winPatterns) {
            val (a, b, c) = pattern
            if (board[a] != null && board[a] == board[b] && board[a] == board[c]) {
                return true
            }
        }
        return false
    }

    fun resetGame() {
        board.fill(null)
        currentPlayer = 'X'
        winner = null
        isGameOver = false
    }
}

@Composable
fun TicTacToeScreen(
    onBack: () -> Unit = {},
    ticTacToeViewModel: TicTacToeViewModel = viewModel()
) {
    val board = ticTacToeViewModel.board
    val currentPlayer = ticTacToeViewModel.currentPlayer
    val winner = ticTacToeViewModel.winner
    val isGameOver = ticTacToeViewModel.isGameOver

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
                "3 en Raya",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Juegas como X contra la CPU",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Game Board
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (r in 0 until 3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (c in 0 until 3) {
                            val index = r * 3 + c
                            val cell = board[index]
                            CellView(
                                cell = cell,
                                onClick = { 
                                    ticTacToeViewModel.makeMove(index)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Status
            when {
                winner != null -> {
                    Text(
                        "¡${winner} gana! 🎉",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (winner == 'X') NeonBlue else NeonPink
                    )
                }
                isGameOver -> {
                    Text(
                        "¡Empate! 🤝",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                else -> {
                    Text(
                        "Turno: $currentPlayer",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Restart button
            Button(
                onClick = { ticTacToeViewModel.resetGame() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonBlue
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    "Reiniciar Juego",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun CellView(
    cell: Char?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick, enabled = cell == null),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = if (cell != null) {
            androidx.compose.foundation.BorderStroke(
                2.dp,
                if (cell == 'X') NeonBlue else NeonPink
            )
        } else null
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (cell != null) {
                Text(
                    text = cell.toString(),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (cell == 'X') NeonBlue else NeonPink
                )
            }
        }
    }
}

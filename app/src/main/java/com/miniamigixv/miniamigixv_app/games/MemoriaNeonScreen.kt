package com.miniamigixv.miniamigixv_app.games

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

private val NeonPurple = Color(0xFF8B5CF6)
private val NeonPink = Color(0xFFEC4899)

data class Card(
    val id: Int,
    val emoji: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)

@Composable
fun MemoriaNeonScreen(
    onBack: () -> Unit = {}
) {
    var cards by remember { mutableStateOf<List<Card>>(emptyList()) }
    var moves by remember { mutableStateOf(0) }
    var flippedCards by remember { mutableStateOf<List<Card>>(emptyList()) }
    var isChecking by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun initializeGame() {
        val emojis = listOf("🌟", "🔥", "💎", "🎵", "🌈", "⚡", "🍀", "💖")
        val cardPairs = emojis.flatMap { emoji ->
            listOf(
                Card(id = Random.nextInt(), emoji = emoji),
                Card(id = Random.nextInt(), emoji = emoji)
            )
        }.shuffled()
        cards = cardPairs
        moves = 0
        flippedCards = emptyList()
        isChecking = false
    }

    LaunchedEffect(Unit) {
        initializeGame()
    }

    fun onCardClick(card: Card) {
        if (isChecking || card.isFlipped || card.isMatched || flippedCards.size >= 2) return

        val updatedCards = cards.map { if (it.id == card.id) it.copy(isFlipped = true) else it }
        cards = updatedCards

        val newFlippedCards = flippedCards + card
        flippedCards = newFlippedCards

        if (newFlippedCards.size == 2) {
            isChecking = true
            moves++

            if (newFlippedCards[0].emoji == newFlippedCards[1].emoji) {
                cards = cards.map {
                    if (it.id == newFlippedCards[0].id || it.id == newFlippedCards[1].id) {
                        it.copy(isMatched = true)
                    } else it
                }
                flippedCards = emptyList()
                isChecking = false
            } else {
                coroutineScope.launch {
                    delay(1000)
                    cards = cards.map {
                        if (it.id == newFlippedCards[0].id || it.id == newFlippedCards[1].id) {
                            it.copy(isFlipped = false)
                        } else it
                    }
                    flippedCards = emptyList()
                    isChecking = false
                }
            }
        }
    }

    val allMatched = cards.all { it.isMatched }

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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(NeonPurple, NeonPink)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🧠", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Memoria Neón",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Encuentra los pares de cartas idénticas.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Game Grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                cards.chunked(4).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { card ->
                            CardView(
                                card = card,
                                onClick = { onCardClick(card) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Moves counter
            Text(
                "Movimientos: $moves",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Restart button
            Button(
                onClick = { initializeGame() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonPurple
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

            // Win message
            if (allMatched) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = NeonPurple.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "¡Ganaste! 🎉",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Completado en $moves movimientos",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CardView(
    card: Card,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(NeonPurple, NeonPink)
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick, enabled = !card.isMatched)
    ) {
        AnimatedContent(
            targetState = card.isFlipped || card.isMatched,
            transitionSpec = {
                slideInHorizontally(
                    animationSpec = tween(300)
                ) { fullWidth -> fullWidth } + fadeIn(
                    animationSpec = tween(300)
                ) togetherWith slideOutHorizontally(
                    animationSpec = tween(300)
                ) { fullWidth -> -fullWidth } + fadeOut(
                    animationSpec = tween(300)
                )
            },
            label = "cardFlip"
        ) { isFlipped ->
            if (isFlipped) {
                // Card face
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(gradient)
                        .border(
                            2.dp,
                            Color.White.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        card.emoji,
                        fontSize = 36.sp
                    )
                }
            } else {
                // Card back
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    NeonPurple.copy(alpha = 0.3f),
                                    NeonPink.copy(alpha = 0.3f)
                                )
                            )
                        )
                        .border(
                            2.dp,
                            NeonPurple.copy(alpha = 0.5f),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "?",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

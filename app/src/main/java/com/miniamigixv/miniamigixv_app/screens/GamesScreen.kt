package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miniamigixv.miniamigixv_app.screens.data.model.Game
import com.miniamigixv.miniamigixv_app.ui.components.GlassCard

private val NeonPurple = Color(0xFF8B5CF6)
private val NeonBlue = Color(0xFF06B6D4)
private val NeonCyan = Color(0xFF22D3EE)
private val NeonPink = Color(0xFFF472B6)
private val NeonGreen = Color(0xFF10B981)
private val NeonOrange = Color(0xFFF59E0B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    onBack: () -> Unit = {},
    onNavigateToMemoriaNeon: () -> Unit = {},
    onNavigateToRespiracionConsciente: () -> Unit = {},
    onNavigateToSnakeNeo: () -> Unit = {},
    onNavigateToTicTacToe: () -> Unit = {},
    gamesViewModel: GamesViewModel = viewModel()
) {
    val games = gamesViewModel.games
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("all") }

    val categories = listOf(
        "all" to "Todos",
        "inteligencia" to "Inteligencia",
        "velocidad" to "Velocidad",
        "precision" to "Precision",
        "estrategia" to "Estrategia",
        "relax" to "Relax"
    )

    val filteredGames = remember(games, searchQuery, selectedCategory) {
        games.filter { game ->
            val matchesSearch = searchQuery.isBlank() ||
                game.title.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "all" ||
                game.tag.lowercase().contains(selectedCategory)
            matchesSearch && matchesCategory
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF09090F), Color(0xFF0F172A)),
                    center = Offset(0f, 0f),
                    radius = 1200f
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            TopAppBar(
                title = { Text("Juegos Arena", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF09090F))
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HeroSection()
                StatsRow(gamesCount = games.size)
                SearchFiltersSection(
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelect = { selectedCategory = it }
                )
                GamesGrid(
                    games = filteredGames,
                    onGameClick = { game ->
                        when (game.id) {
                            "3" -> onNavigateToMemoriaNeon()
                            "8" -> onNavigateToRespiracionConsciente()
                            "7" -> onNavigateToSnakeNeo()
                            "9" -> onNavigateToTicTacToe()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun HeroSection() {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🎮", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Juegos Arena",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                "Pon a prueba tu mente, velocidad y memoria",
                fontSize = 13.sp,
                color = Color(0xFF94A3B8),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun StatsRow(gamesCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val stats = listOf(
            Triple("🎮", "$gamesCount", "Juegos"),
            Triple("🏆", "0", "Record"),
            Triple("🔥", "0", "Hoy"),
            Triple("⏱", "0 min", "Tiempo")
        )
        stats.forEach { (icon, value, label) ->
            GlassCard(modifier = Modifier.weight(1f), cornerRadius = 16.dp) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(icon, fontSize = 20.sp)
                    Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(label, fontSize = 10.sp, color = Color(0xFF94A3B8))
                }
            }
        }
    }
}

@Composable
private fun SearchFiltersSection(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    categories: List<Pair<String, String>>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar juego...", color = Color(0xFF64748B), fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedBorderColor = NeonBlue.copy(alpha = 0.5f),
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                cursorColor = NeonBlue,
                focusedContainerColor = Color.White.copy(alpha = 0.05f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.take(4).forEach { (key, label) ->
                CategoryFilterChip(key, label, selectedCategory, onCategorySelect)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.drop(4).forEach { (key, label) ->
                CategoryFilterChip(key, label, selectedCategory, onCategorySelect)
            }
        }
    }
}

@Composable
private fun CategoryFilterChip(key: String, label: String, selectedCategory: String, onCategorySelect: (String) -> Unit) {
    val isSel = key == selectedCategory
    val bgMod = if (isSel) {
        Modifier.background(Brush.horizontalGradient(listOf(NeonPurple.copy(alpha = 0.3f), NeonBlue.copy(alpha = 0.2f))))
    } else {
        Modifier.background(Color.White.copy(alpha = 0.05f))
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .then(bgMod)
            .border(1.dp, if (isSel) NeonPurple.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            .clickable { onCategorySelect(key) }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(label, fontSize = 12.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal, color = if (isSel) Color.White else Color(0xFF94A3B8))
    }
}

@Composable
private fun GamesGrid(games: List<Game>, onGameClick: (Game) -> Unit) {
    if (games.isEmpty()) {
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🎮", fontSize = 48.sp)
                Text("No se encontraron juegos", color = Color(0xFF94A3B8), fontSize = 14.sp)
            }
        }
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        games.forEach { game ->
            GameCard(game = game, onClick = { onGameClick(game) })
        }
    }
}

@Composable
private fun GameCard(game: Game, onClick: () -> Unit = {}) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(NeonPurple.copy(alpha = 0.4f), NeonBlue.copy(alpha = 0.3f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(game.icon, contentDescription = null, modifier = Modifier.size(32.dp), tint = Color.White)
            }

            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonPurple.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(game.tag, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonPurple)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(game.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
                Text(game.description, fontSize = 11.sp, color = Color(0xFF94A3B8), maxLines = 2, lineHeight = 14.sp)
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.horizontalGradient(listOf(NeonPurple, NeonBlue))),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Jugar", tint = Color.White, modifier = Modifier.size(24.dp))
            }
        }
    }
}

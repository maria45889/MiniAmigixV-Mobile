package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.GlassCard
import com.miniamigixv.miniamigixv_app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntertainmentScreen(themeViewModel: ThemeViewModel, onBack: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("all") }

    val categories = listOf(
        "all" to "Todas",
        "peliculas" to "Películas",
        "series" to "Series",
        "libros" to "Libros",
        "anime" to "Anime",
        "teatro" to "Teatro",
        "documentales" to "Documentales"
    )

    val categoryIcons = mapOf(
        "all" to Icons.Filled.Explore,
        "peliculas" to Icons.Filled.MovieCreation,
        "series" to Icons.Filled.Tv,
        "libros" to Icons.Filled.MenuBook,
        "anime" to Icons.Filled.SportsMartialArts,
        "teatro" to Icons.Filled.TheaterComedy,
        "documentales" to Icons.Filled.VideoLibrary
    )

    val featuredBgColors = listOf(
        NeonViolet.copy(alpha = 0.15f),
        NeonBlue.copy(alpha = 0.15f),
        NeonPink.copy(alpha = 0.15f),
        NeonGreen.copy(alpha = 0.15f)
    )

    val carouselItems = mapOf(
        "peliculas" to listOf("Inception", "Interstellar", "The Matrix", "Dune"),
        "series" to listOf("Dark", "Stranger Things", "The Crown", "Arcane"),
        "libros" to listOf("1984", "Dune", "Neuromancer", "Fahrenheit 451"),
        "anime" to listOf("Akira", "Ghost in Shell", "Your Name", "Mononoke"),
        "teatro" to listOf("Hamilton", "El Rey León", "Wicked", "Les Mis"),
        "documentales" to listOf("Planet Earth", "Cosmos", "13th", "Our Planet")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF09090F), Color(0xFF0F172A))
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        "Entretenimiento",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
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
                    containerColor = Color.Transparent
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                GlassCard {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(NeonViolet.copy(alpha = 0.15f))
                                .padding(horizontal = 14.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "🎬 RECOMENDADO",
                                color = NeonViolet,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "🎭",
                            fontSize = 40.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Descubre nuevo contenido",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Explora películas, series, libros y más",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 13.sp
                        )
                    }
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    placeholder = {
                        Text(
                            "Buscar contenido...",
                            color = Color.White.copy(alpha = 0.3f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.4f)
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonViolet.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = NeonViolet,
                        focusedContainerColor = Color.White.copy(alpha = 0.04f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.04f)
                    ),
                    shape = RoundedCornerShape(14.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { (key, label) ->
                        val selected = selectedCategory == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (selected) NeonViolet.copy(alpha = 0.2f)
                                    else Color.White.copy(alpha = 0.05f)
                                )
                                .clickable { selectedCategory = key }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    categoryIcons[key] ?: Icons.Filled.Explore,
                                    contentDescription = null,
                                    tint = if (selected) NeonViolet else Color.White.copy(alpha = 0.5f),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    label,
                                    color = if (selected) NeonViolet else Color.White.copy(alpha = 0.6f),
                                    fontSize = 12.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                GlassCard {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = NeonOrange,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "RECOMENDACIÓN DESTACADA",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            NeonViolet.copy(alpha = 0.2f),
                                            NeonBlue.copy(alpha = 0.2f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(NeonOrange.copy(alpha = 0.3f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        "PELÍCULA",
                                        color = NeonOrange,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Dune: Part Two",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "★ 8.9 · 2024 · Ciencia Ficción",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                val itemsToShow = if (selectedCategory == "all") {
                    carouselItems.entries.toList()
                } else {
                    carouselItems.filterKeys { it == selectedCategory }.entries.toList()
                }

                itemsToShow.forEach { (category, items) ->
                    GlassCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                category.replaceFirstChar { it.uppercase() },
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Ver todo",
                                color = NeonViolet,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(items) { item ->
                                Box(
                                    modifier = Modifier
                                        .width(130.dp)
                                        .height(170.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            featuredBgColors[
                                                items.indexOf(item) % featuredBgColors.size
                                            ]
                                        ),
                                    contentAlignment = Alignment.BottomStart
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.White.copy(alpha = 0.1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                categoryIcons[category] ?: Icons.Filled.Movie,
                                                contentDescription = null,
                                                tint = Color.White.copy(alpha = 0.6f),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            item,
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            "★ 8.5",
                                            color = NeonOrange,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

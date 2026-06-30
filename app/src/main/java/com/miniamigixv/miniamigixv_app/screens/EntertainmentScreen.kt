package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntertainmentScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Entretenimiento", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                BannerSection()
            }

            item {
                CategorySection()
            }

            item {
                TrendingSection()
            }
        }
    }
}

@Composable
private fun BannerSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        NeonCard(padding = 24.dp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Movie, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Catálogo de", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                    Text("Entretenimiento", color = MaterialTheme.colorScheme.onBackground, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MetricItem("Películas", "120+", NeonCyan)
                MetricItem("Series", "85+", NeonPurple)
                MetricItem("Libros", "40+", NeonCyan)
            }
        }
    }
}

@Composable
private fun MetricItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable
private fun CategorySection() {
    Column {
        PaddingValues(horizontal = 16.dp).let {
            Text("Explorar por categoría", color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        
        val categories = listOf(
            Pair("Películas", Icons.Filled.MovieCreation),
            Pair("Series", Icons.Filled.Tv),
            Pair("Libros", Icons.Filled.MenuBook),
            Pair("Teatro", Icons.Filled.TheaterComedy),
            Pair("Anime", Icons.Filled.SportsMartialArts)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Column {
                        Icon(category.second, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(category.first, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Explorar", color = NeonPurple, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun TrendingSection() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
            Icon(Icons.Filled.LocalFireDepartment, contentDescription = null, tint = Color(0xFFF97316))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Tendencias", color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Surface(color = Color(0xFFEF4444), shape = RoundedCornerShape(4.dp)) {
                Text("NEW", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        val trending = listOf(
            Pair("1. Interstellar", "★ 9.2"),
            Pair("2. Dark", "★ 9.0"),
            Pair("3. Inception", "★ 8.8"),
            Pair("4. Breaking Bad", "★ 9.5")
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(trending.size) { index ->
                val item = trending[index]
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (index % 2 == 0) NeonPurple.copy(alpha = 0.2f) else NeonCyan.copy(alpha = 0.2f))
                ) {
                    // Placeholder for Poster
                    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))
                    
                    Column(
                        modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
                    ) {
                        Text(item.first, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
                        Text(item.second, color = Color(0xFFFBBF24), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

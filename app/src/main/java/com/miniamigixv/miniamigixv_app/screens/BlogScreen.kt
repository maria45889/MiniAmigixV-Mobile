package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogScreen(onBack: () -> Unit = {}) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Noticias Globales", "Mis Publicaciones")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        TopAppBar(
            title = { Text("Blog", fontWeight = FontWeight.Bold, color = TextWhite) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = TextWhite)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = CardBg)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                NeonHeader(title = "BLOG MINIAMIGIXV", subtitle = "Comparte ideas y descubre noticias")
            }

            item {
                NeonButton(
                    text = "+ NUEVA PUBLICACIÓN",
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = NeonCyan,
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                color = NeonCyan
                            )
                        }
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { 
                                Text(
                                    title, 
                                    color = if (selectedTabIndex == index) NeonCyan else TextGray,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                ) 
                            }
                        )
                    }
                }
            }

            item {
                if (selectedTabIndex == 0) {
                    // Noticias Globales
                    GlobalNewsList()
                } else {
                    // Mis Publicaciones
                    MyPostsList()
                }
            }
        }
    }
}

@Composable
private fun GlobalNewsList() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        BlogPostCard("El futuro de la IA", "MiniAmigixV Admin", "Descubre las nuevas tendencias y cómo la inteligencia artificial está redefiniendo el mundo.")
        BlogPostCard("Guía de Productividad", "Comunidad", "5 tips para mejorar tu rendimiento diario usando herramientas digitales.")
    }
}

@Composable
private fun MyPostsList() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("No tienes publicaciones aún", color = TextGray)
        }
    }
}

@Composable
private fun BlogPostCard(title: String, author: String, excerpt: String) {
    NeonCard {
        Text(title, color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Por $author", color = NeonPurple, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(excerpt, color = TextGray, fontSize = 14.sp)
    }
}

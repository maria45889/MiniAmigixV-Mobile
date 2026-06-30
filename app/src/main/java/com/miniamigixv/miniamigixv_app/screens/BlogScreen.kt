package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miniamigixv.miniamigixv_app.screens.data.model.BlogPost
import com.miniamigixv.miniamigixv_app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogScreen(
    onBack: () -> Unit = {},
    blogViewModel: BlogViewModel = viewModel()
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Noticias Globales", "Mis Publicaciones")
    val myPosts = blogViewModel.myPosts
    val showCreateDialog = blogViewModel.showCreatePostDialog

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Blog", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
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
                    onClick = { blogViewModel.toggleCreatePostDialog(true) },
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
                                    color = if (selectedTabIndex == index) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant,
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
                    MyPostsList(myPosts, blogViewModel)
                }
            }
        }

        if (showCreateDialog) {
            CreateBlogPostDialog(
                onDismiss = { blogViewModel.toggleCreatePostDialog(false) },
                onConfirm = { title, content, category, imageUrl, visibleToAll, isOfficial, isPinned ->
                    blogViewModel.addPost(title, content, category, imageUrl, visibleToAll, isOfficial, isPinned)
                }
            )
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
private fun MyPostsList(posts: List<BlogPost>, blogViewModel: BlogViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (posts.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No tienes publicaciones aún", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            posts.forEach { post ->
                BlogPostCard(post.title, post.author, post.content)
            }
        }
    }
}

@Composable
private fun BlogPostCard(title: String, author: String, excerpt: String) {
    NeonCard {
        Text(title, color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Por $author", color = NeonPurple, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(excerpt, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBlogPostDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String?, Boolean, Boolean, Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("General") }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var visibleToAll by remember { mutableStateOf(true) }
    var isOfficial by remember { mutableStateOf(false) }
    var isPinned by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val categories = listOf("General", "Tecnología", "Educación", "Entretenimiento", "Noticias")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "Crear Nueva Publicación",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    )
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Contenido", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    maxLines = 8,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    )
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                var categoryExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat, color = MaterialTheme.colorScheme.onBackground) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = imageUrl ?: "",
                    onValueChange = { imageUrl = if (it.isBlank()) null else it },
                    label = { Text("Imagen (URL - opcional)", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = visibleToAll,
                        onCheckedChange = { visibleToAll = it },
                        colors = CheckboxDefaults.colors(checkedColor = NeonPurple)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Visible para todos", color = MaterialTheme.colorScheme.onBackground)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isOfficial,
                        onCheckedChange = { isOfficial = it },
                        colors = CheckboxDefaults.colors(checkedColor = NeonPurple)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Publicación oficial", color = MaterialTheme.colorScheme.onBackground)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isPinned,
                        onCheckedChange = { isPinned = it },
                        colors = CheckboxDefaults.colors(checkedColor = NeonPurple)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Fijar publicación", color = MaterialTheme.colorScheme.onBackground)
                }
                
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        errorMessage,
                        color = Color(0xFFEF4444),
                        fontSize = 12.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                errorMessage = "El título es requerido"
                                return@Button
                            }
                            if (content.isBlank()) {
                                errorMessage = "El contenido es requerido"
                                return@Button
                            }
                            onConfirm(title, content, category, imageUrl, visibleToAll, isOfficial, isPinned)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                    ) {
                        Text("Publicar", color = Color.White)
                    }
                }
            }
        }
    }
}

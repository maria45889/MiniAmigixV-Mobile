package com.miniamigixv.miniamigixv_app.screens.data.model

data class BlogPost(
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val imageUrl: String? = null,
    val visibleToAll: Boolean = true,
    val isOfficial: Boolean = false,
    val isPinned: Boolean = false,
    val author: String = "Usuario",
    val timestamp: Long = System.currentTimeMillis()
)

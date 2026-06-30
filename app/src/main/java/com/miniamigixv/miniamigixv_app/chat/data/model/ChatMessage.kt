package com.miniamigixv.miniamigixv_app.chat.data.model

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUrl: String? = null
)

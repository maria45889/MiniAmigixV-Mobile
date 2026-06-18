package com.miniamigixv.miniamigixv_app.chat.data.remote

data class ChatRequest(
    val message: String,
    val history: List<ChatHistoryItem> = emptyList()
)

data class ChatHistoryItem(
    val role: String,
    val content: String
)

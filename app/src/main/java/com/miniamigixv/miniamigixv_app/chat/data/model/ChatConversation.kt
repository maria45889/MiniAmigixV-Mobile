package com.miniamigixv.miniamigixv_app.chat.data.model

data class ChatConversation(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: Long,
    val unreadCount: Int = 0
)

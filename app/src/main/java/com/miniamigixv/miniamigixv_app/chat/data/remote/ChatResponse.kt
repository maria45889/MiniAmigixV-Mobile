package com.miniamigixv.miniamigixv_app.chat.data.remote

data class ChatResponse(
    val reply: String,
    val timestamp: Long = System.currentTimeMillis()
)

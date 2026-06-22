package com.miniamigixv.miniamigixv_app.chat.data.remote

data class ChatResponse(
    val response: String,
    val timestamp: Long = System.currentTimeMillis()
)


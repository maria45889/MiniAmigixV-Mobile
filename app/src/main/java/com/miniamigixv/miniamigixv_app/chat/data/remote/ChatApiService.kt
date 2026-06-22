package com.miniamigixv.miniamigixv_app.chat.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API service for your backend.
 *
 * Backend endpoint expected:
 *   POST /chat
 *   Body: { "message": "texto del usuario" }
 *   Response: { "reply": "respuesta de la IA" }
 *
 * Recommended backend stack:
 *   Node.js + Express → OpenAI API
 *   Python + FastAPI  → OpenAI API
 */
import retrofit2.http.GET

interface ChatApiService {

    @POST("api/chat/send/")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): ChatResponse
    
    @GET("api/chat/history/")
    suspend fun getHistory(): List<ChatHistoryItem>
}

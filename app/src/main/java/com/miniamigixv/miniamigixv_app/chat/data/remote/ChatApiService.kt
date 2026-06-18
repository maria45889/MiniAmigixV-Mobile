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
interface ChatApiService {

    @POST("chat")
    suspend fun sendMessage(
        @Body request: ChatRequest
    ): ChatResponse
}

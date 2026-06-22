package com.miniamigixv.miniamigixv_app.chat.data.repository

import com.miniamigixv.miniamigixv_app.chat.data.model.ChatMessage
import com.miniamigixv.miniamigixv_app.chat.data.remote.ChatApiClient
import com.miniamigixv.miniamigixv_app.chat.data.remote.ChatApiConfig
import com.miniamigixv.miniamigixv_app.chat.data.remote.ChatRequest
import com.miniamigixv.miniamigixv_app.chat.data.remote.MockChatApiService
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit

class ChatRepository {

    private val mockApi = MockChatApiService()
    private val realApi = ChatApiClient.apiService

    suspend fun sendMessage(
        message: String,
        history: List<ChatMessage> = emptyList()
    ): Result<String> {
        if (message.isBlank()) {
            return Result.failure(IllegalArgumentException("El mensaje no puede estar vacío"))
        }

        return try {
            if (ChatApiConfig.useMockApi) {
                withTimeout(TimeUnit.SECONDS.toMillis(ChatApiConfig.TIMEOUT_SECONDS)) {
                    val response = mockApi.sendMessage(ChatRequest(message = message))
                    Result.success(response.response)
                }
            } else {
                val historyItems = history.filter { it.isUser }.map {
                    com.miniamigixv.miniamigixv_app.chat.data.remote.ChatHistoryItem(
                        role = "user",
                        content = it.text,
                        created_at = ""
                    )
                }

                withTimeout(TimeUnit.SECONDS.toMillis(ChatApiConfig.TIMEOUT_SECONDS)) {
                    val response = realApi.sendMessage(
                        ChatRequest(message = message, history = emptyList())
                    )
                    Result.success(response.response)
                }
            }
        } catch (e: TimeoutCancellationException) {
            Result.failure(Exception("La solicitud tardó demasiado. Intenta de nuevo."))
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.localizedMessage ?: "Sin respuesta del servidor"}"))
        }
    }

    suspend fun getHistory(): Result<List<ChatMessage>> {
        return try {
            if (ChatApiConfig.useMockApi) {
                Result.success(emptyList())
            } else {
                withTimeout(TimeUnit.SECONDS.toMillis(ChatApiConfig.TIMEOUT_SECONDS)) {
                    val historyResponse = realApi.getHistory()
                    val chatMessages = historyResponse.map {
                        ChatMessage(
                            id = java.util.UUID.randomUUID().toString(),
                            text = it.content,
                            isUser = it.role == "user",
                            timestamp = System.currentTimeMillis()
                        )
                    }
                    Result.success(chatMessages)
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error al cargar historial: ${e.localizedMessage}"))
        }
    }
}

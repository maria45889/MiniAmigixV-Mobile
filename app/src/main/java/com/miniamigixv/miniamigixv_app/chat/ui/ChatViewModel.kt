package com.miniamigixv.miniamigixv_app.chat.ui

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miniamigixv.miniamigixv_app.chat.data.model.ChatConversation
import com.miniamigixv.miniamigixv_app.chat.data.model.ChatMessage
import com.miniamigixv.miniamigixv_app.chat.data.repository.ChatRepository
import kotlinx.coroutines.launch
import java.util.UUID

sealed class ChatUiState {
    data object Idle : ChatUiState()
    data object Sending : ChatUiState()
    data class Error(val message: String) : ChatUiState()
}

class ChatViewModel : ViewModel() {

    private val repository = ChatRepository()

    var messages by mutableStateOf(listOf<ChatMessage>())
        private set

    var conversations by mutableStateOf(listOf<ChatConversation>())
        private set

    var selectedConversationId by mutableStateOf<String?>(null)
        private set

    private fun loadHistory() {
        viewModelScope.launch {
            uiState = ChatUiState.Sending
            val result = repository.getHistory()
            result.fold(
                onSuccess = { history ->
                    messages = history
                    uiState = ChatUiState.Idle
                },
                onFailure = { error ->
                    uiState = ChatUiState.Error(error.message ?: "Error al cargar historial")
                }
            )
        }
    }

    var uiState by mutableStateOf<ChatUiState>(ChatUiState.Idle)
        private set

    var inputText by mutableStateOf("")
        private set

    private var lastFailedMessage: String? = null

    fun updateInputText(text: String) {
        inputText = text
    }

    fun sendMessage() {
        val text = inputText.trim()
        if (text.isBlank()) return

        sendMessageInternal(text)
        inputText = ""
    }

    fun sendMessageWithImage(imageUri: Uri) {
        val text = inputText.trim()
        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = text,
            isUser = true,
            timestamp = System.currentTimeMillis(),
            imageUrl = imageUri.toString()
        )

        messages = messages + userMessage
        uiState = ChatUiState.Sending
        inputText = ""

        viewModelScope.launch {
            val result = repository.sendMessage(text.ifBlank { "Imagen" }, messages)
            result.fold(
                onSuccess = { reply ->
                    val botMessage = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        text = reply,
                        isUser = false,
                        timestamp = System.currentTimeMillis()
                    )
                    messages = messages + botMessage
                    uiState = ChatUiState.Idle
                    lastFailedMessage = null
                },
                onFailure = { error ->
                    uiState = ChatUiState.Error(error.message ?: "Error al enviar mensaje")
                }
            )
        }
    }

    fun retryLastMessage() {
        val lastText = lastFailedMessage ?: return
        lastFailedMessage = null
        uiState = ChatUiState.Idle
        sendMessageInternal(lastText)
    }

    private fun sendMessageInternal(text: String) {
        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = text,
            isUser = true,
            timestamp = System.currentTimeMillis()
        )

        messages = messages + userMessage
        uiState = ChatUiState.Sending
        lastFailedMessage = text

        viewModelScope.launch {
            val result = repository.sendMessage(text, messages)
            result.fold(
                onSuccess = { reply ->
                    val botMessage = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        text = reply,
                        isUser = false,
                        timestamp = System.currentTimeMillis()
                    )
                    messages = messages + botMessage
                    uiState = ChatUiState.Idle
                    lastFailedMessage = null
                },
                onFailure = { error ->
                    uiState = ChatUiState.Error(error.message ?: "Error al enviar mensaje")
                }
            )
        }
    }

    fun clearMessages() {
        messages = emptyList()
        uiState = ChatUiState.Idle
        lastFailedMessage = null
    }

    fun clearError() {
        uiState = ChatUiState.Idle
    }

    fun selectConversation(conversationId: String) {
        selectedConversationId = conversationId
        messages = emptyList()
        loadHistory()
    }

    fun createNewConversation() {
        val newId = UUID.randomUUID().toString()
        val newConversation = ChatConversation(
            id = newId,
            name = "Nuevo Chat",
            lastMessage = "",
            timestamp = System.currentTimeMillis(),
            unreadCount = 0
        )
        conversations = conversations + newConversation
        selectConversation(newId)
    }

    private fun loadMockConversations() {
        val now = System.currentTimeMillis()
        conversations = listOf(
            ChatConversation(
                id = "1",
                name = "Chat 08:05",
                lastMessage = "Hola, ¿cómo estás?",
                timestamp = now - 3600000,
                unreadCount = 0
            ),
            ChatConversation(
                id = "2",
                name = "Chat 07:11",
                lastMessage = "Gracias por tu ayuda",
                timestamp = now - 7200000,
                unreadCount = 2
            ),
            ChatConversation(
                id = "3",
                name = "Chat 08:56",
                lastMessage = "¿Puedes explicarme más?",
                timestamp = now - 1800000,
                unreadCount = 0
            ),
            ChatConversation(
                id = "4",
                name = "Chat Principal",
                lastMessage = "Inicia una conversación",
                timestamp = now,
                unreadCount = 0
            )
        )
        selectedConversationId = "4"
    }

    init {
        loadMockConversations()
        loadHistory()
    }
}

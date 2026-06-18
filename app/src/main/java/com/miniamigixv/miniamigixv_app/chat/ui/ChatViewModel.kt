package com.miniamigixv.miniamigixv_app.chat.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}

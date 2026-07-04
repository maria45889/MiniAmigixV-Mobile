package com.miniamigixv.miniamigixv_app.chat.data.remote

import kotlinx.coroutines.delay

class MockChatApiService : ChatApiService {

    private val responses = mapOf(
        "hola" to "¡Hola! ¿En qué puedo ayudarte hoy?",
        "qué eres" to "Soy MiniAmigixV, tu asistente virtual. Puedo ayudarte con información, conversación y más.",
        "quién eres" to "Soy MiniAmigixV, tu asistente virtual. Puedo ayudarte con información, conversación y más.",
        "clima" to "Puedes consultar el clima desde la sección Clima en el menú principal. Allí puedes buscar cualquier ciudad.",
        "música" to "La sección de Música te permite explorar tus canciones favoritas. Pronto tendremos más funciones.",
        "gracias" to "¡De nada! Estoy aquí para ayudarte 😊",
        "adiós" to "¡Hasta luego! Vuelve cuando quieras 🚀",
        "ayuda" to "Puedes usar las secciones: Clima, Música, Chat IA y Perfil desde el menú principal. ¿Sobre qué necesitas ayuda?",
        "ais" to "Sí, algo así. ¿Quieres que te lo explique con otras palabras?",
        "algo así" to "Sí, eso mismo. Puedo decirlo de otra forma si lo necesitas.",
        "entiendes" to "Sí, te entiendo. Dime si quieres que lo aclare o que lo explique de un modo distinto."
    )

    override suspend fun sendMessage(request: ChatRequest): ChatResponse {
        delay(1500)

        val lower = request.message.lowercase().trim()
        val matched = responses.entries.firstOrNull { (key, _) -> lower.contains(key) }
        val response = matched?.value ?: "Interesante tu mensaje. Soy MiniAmigixV, tu asistente. Cuéntame más sobre eso."

        return ChatResponse(response = response)
    }

    override suspend fun getHistory(): List<com.miniamigixv.miniamigixv_app.chat.data.remote.ChatHistoryItem> {
        return emptyList()
    }
}

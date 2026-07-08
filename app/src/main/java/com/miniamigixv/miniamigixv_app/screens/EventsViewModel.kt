package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.miniamigixv.miniamigixv_app.screens.data.model.Event
import java.util.UUID

class EventsViewModel : ViewModel() {
    var events by mutableStateOf(listOf<Event>())
        private set

    var showCreateEventDialog by mutableStateOf(false)
        private set

    init {
        loadMockEvents()
    }

    private fun loadMockEvents() {
        val now = System.currentTimeMillis()
        events = listOf(
            Event(
                id = UUID.randomUUID().toString(),
                title = "Reunión de equipo",
                description = "Sala Virtual",
                dateTime = now + 3600000,
                location = "Zoom",
                category = "reunion",
                reminderActive = true,
                reminderMinutesBefore = 15
            ),
            Event(
                id = UUID.randomUUID().toString(),
                title = "Estudiar Matemáticas",
                description = "Temas: Integrales",
                dateTime = now + 7200000,
                location = "Biblioteca",
                category = "tarea",
                reminderActive = true,
                reminderMinutesBefore = 30
            ),
            Event(
                id = UUID.randomUUID().toString(),
                title = "Proyecto Final",
                description = "Entrega de reporte",
                dateTime = now + 10800000,
                location = "Aula 101",
                category = "evento",
                reminderActive = false,
                reminderMinutesBefore = 60
            )
        )
    }

    fun toggleCreateEventDialog(show: Boolean) {
        showCreateEventDialog = show
    }

    fun addEvent(title: String, description: String, dateTime: Long, location: String, category: String = "personal", reminderActive: Boolean = false, reminderMinutesBefore: Int = 30) {
        val newEvent = Event(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            dateTime = dateTime,
            location = location,
            category = category,
            reminderActive = reminderActive,
            reminderMinutesBefore = reminderMinutesBefore
        )
        events = events + newEvent
        showCreateEventDialog = false
    }

    fun deleteEvent(eventId: String) {
        events = events.filter { it.id != eventId }
    }
}

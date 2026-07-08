package com.miniamigixv.miniamigixv_app.screens.data.model

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val dateTime: Long,
    val location: String = "",
    val category: String = "personal",
    val reminderActive: Boolean = false,
    val reminderMinutesBefore: Int = 30,
    val createdAt: Long = System.currentTimeMillis()
)

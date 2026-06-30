package com.miniamigixv.miniamigixv_app.screens.data.model

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val dateTime: Long,
    val location: String = ""
)

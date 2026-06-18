package com.miniamigixv.miniamigixv_app.music.data.model

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val durationMs: Long,
    val coverUrl: String? = null,
    val audioUrl: String? = null
)

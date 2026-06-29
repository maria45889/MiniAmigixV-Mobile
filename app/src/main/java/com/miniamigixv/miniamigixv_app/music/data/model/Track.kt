package com.miniamigixv.miniamigixv_app.music.data.model

data class Track(
    val id: String,
    val name: String,
    val artist: String = "Artista Desconocido",
    val youtubeLink: String? = null,
    val neuralFrequency: String = "432 Hz",
    val isSelected: Boolean = false
)

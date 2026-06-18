package com.miniamigixv.miniamigixv_app.music.data.repository

import com.miniamigixv.miniamigixv_app.music.data.model.Song

class MusicRepository {

    private val playlist = listOf(
        Song("1", "Blinding Lights", "The Weeknd", 200_000, audioUrl = null),
        Song("2", "Shape of You", "Ed Sheeran", 233_000, audioUrl = null),
        Song("3", "Bohemian Rhapsody", "Queen", 354_000, audioUrl = null),
        Song("4", "Billie Jean", "Michael Jackson", 294_000, audioUrl = null),
        Song("5", "Hotel California", "Eagles", 391_000, audioUrl = null),
        Song("6", "Imagine", "John Lennon", 187_000, audioUrl = null),
        Song("7", "Smells Like Teen Spirit", "Nirvana", 301_000, audioUrl = null),
        Song("8", "Rolling in the Deep", "Adele", 228_000, audioUrl = null),
        Song("9", "Stairway to Heaven", "Led Zeppelin", 482_000, audioUrl = null),
        Song("10", "Yesterday", "The Beatles", 125_000, audioUrl = null)
    )

    fun getPlaylist(): List<Song> = playlist

    fun getSongById(id: String): Song? = playlist.find { it.id == id }
}

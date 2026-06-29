package com.miniamigixv.miniamigixv_app.music.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miniamigixv.miniamigixv_app.music.data.model.Song
import com.miniamigixv.miniamigixv_app.music.data.model.Track
import com.miniamigixv.miniamigixv_app.music.data.player.MusicPlayerManager
import com.miniamigixv.miniamigixv_app.music.data.repository.MusicRepository
import kotlinx.coroutines.launch
import java.util.UUID

data class MusicUiState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val progress: Float = 0f,
    val currentPositionMs: Long = 0,
    val durationMs: Long = 0,
    val isShuffleEnabled: Boolean = false,
    val isRepeatEnabled: Boolean = false,
    val playlist: List<Song> = emptyList(),
    val currentIndex: Int = -1,
    val tracks: List<Track> = emptyList(),
    val selectedTrackId: String? = null,
    val currentTrackName: String = "Selecciona una pista",
    val currentArtist: String = "Artista Desconocido"
)

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MusicRepository()
    val playerManager = MusicPlayerManager(application)

    var state by mutableStateOf(MusicUiState())
        private set

    init {
        val songs = repository.getPlaylist()
        state = state.copy(playlist = songs)
        loadMockTracks()

        viewModelScope.launch {
            playerManager.getProgressFlow().collect { progress ->
                state = state.copy(
                    progress = progress,
                    isPlaying = playerManager.isPlaying,
                    currentPositionMs = playerManager.currentPositionMs,
                    durationMs = playerManager.durationMs
                )
            }
        }
    }

    private fun loadMockTracks() {
        val now = System.currentTimeMillis()
        state = state.copy(
            tracks = listOf(
                Track(
                    id = "1",
                    name = "Cosmic Journey",
                    artist = "Neural Waves",
                    neuralFrequency = "432 Hz",
                    isSelected = true
                ),
                Track(
                    id = "2",
                    name = "Deep Focus",
                    artist = "Mind Sync",
                    neuralFrequency = "528 Hz",
                    isSelected = false
                ),
                Track(
                    id = "3",
                    name = "Alpha Waves",
                    artist = "Brain Harmony",
                    neuralFrequency = "396 Hz",
                    isSelected = false
                ),
                Track(
                    id = "4",
                    name = "Theta Meditation",
                    artist = "Soul Frequency",
                    neuralFrequency = "639 Hz",
                    isSelected = false
                )
            ),
            selectedTrackId = "1",
            currentTrackName = "Cosmic Journey",
            currentArtist = "Neural Waves"
        )
    }

    fun selectTrack(trackId: String) {
        val track = state.tracks.find { it.id == trackId }
        if (track != null) {
            state = state.copy(
                selectedTrackId = trackId,
                currentTrackName = track.name,
                currentArtist = track.artist,
                tracks = state.tracks.map { it.copy(isSelected = it.id == trackId) }
            )
        }
    }

    fun addTrack(name: String, artist: String, youtubeLink: String?) {
        val newTrack = Track(
            id = UUID.randomUUID().toString(),
            name = name,
            artist = artist.ifBlank { "Artista Desconocido" },
            youtubeLink = youtubeLink,
            neuralFrequency = "432 Hz",
            isSelected = false
        )
        state = state.copy(tracks = state.tracks + newTrack)
    }

    fun deleteTrack(trackId: String) {
        state = state.copy(tracks = state.tracks.filter { it.id != trackId })
    }

    fun searchLyricsOnGoogle() {
        // Implementation for opening Google search with lyrics
    }

    fun playSong(song: Song) {
        val index = state.playlist.indexOfFirst { it.id == song.id }
        playerManager.play(song)
        state = state.copy(
            currentSong = song,
            currentIndex = index,
            isPlaying = true,
            progress = 0f,
            currentPositionMs = 0,
            durationMs = song.durationMs
        )
    }

    fun togglePlayPause() {
        playerManager.togglePlayPause()
        state = state.copy(isPlaying = playerManager.isPlaying)
    }

    fun playNext() {
        val current = state.currentIndex
        val playlist = state.playlist
        if (current < 0 || playlist.isEmpty()) return

        val nextIndex = if (state.isShuffleEnabled) {
            (playlist.indices).random()
        } else {
            (current + 1) % playlist.size
        }
        playSong(playlist[nextIndex])
    }

    fun playPrevious() {
        val current = state.currentIndex
        val playlist = state.playlist
        if (current < 0 || playlist.isEmpty()) return

        val prevIndex = if (state.isShuffleEnabled) {
            (playlist.indices).random()
        } else {
            (current - 1 + playlist.size) % playlist.size
        }
        playSong(playlist[prevIndex])
    }

    fun toggleShuffle() {
        val new = !state.isShuffleEnabled
        playerManager.isShuffleEnabled = new
        state = state.copy(isShuffleEnabled = new)
    }

    fun toggleRepeat() {
        val new = !state.isRepeatEnabled
        playerManager.isRepeatEnabled = new
        state = state.copy(isRepeatEnabled = new)
    }

    fun seekTo(positionMs: Long) {
        playerManager.seekTo(positionMs)
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}

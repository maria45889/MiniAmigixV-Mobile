package com.miniamigixv.miniamigixv_app.music.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.miniamigixv.miniamigixv_app.music.data.model.Song
import com.miniamigixv.miniamigixv_app.music.data.player.MusicPlayerManager
import com.miniamigixv.miniamigixv_app.music.data.repository.MusicRepository
import kotlinx.coroutines.launch

data class MusicUiState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val progress: Float = 0f,
    val currentPositionMs: Long = 0,
    val durationMs: Long = 0,
    val isShuffleEnabled: Boolean = false,
    val isRepeatEnabled: Boolean = false,
    val playlist: List<Song> = emptyList(),
    val currentIndex: Int = -1
)

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MusicRepository()
    val playerManager = MusicPlayerManager(application)

    var state by mutableStateOf(MusicUiState())
        private set

    init {
        val songs = repository.getPlaylist()
        state = state.copy(playlist = songs)

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

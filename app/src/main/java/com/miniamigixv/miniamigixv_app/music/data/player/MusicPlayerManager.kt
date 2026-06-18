package com.miniamigixv.miniamigixv_app.music.data.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.miniamigixv.miniamigixv_app.music.data.model.Song
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class MusicPlayerManager(context: Context) {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()

    var isPlaying: Boolean = false
        private set

    var currentPositionMs: Long = 0
        private set

    var durationMs: Long = 0
        private set

    var isShuffleEnabled: Boolean = false
    var isRepeatEnabled: Boolean = false

    private var _currentSong: Song? = null
    val currentSong: Song? get() = _currentSong

    @OptIn(UnstableApi::class)
    fun play(song: Song) {
        _currentSong = song
        val metadata = MediaMetadata.Builder()
            .setTitle(song.title)
            .setArtist(song.artist)
            .build()
        val mediaItem = MediaItem.Builder()
            .setMediaId(song.id)
            .setUri(song.audioUrl ?: "")
            .setMediaMetadata(metadata)
            .build()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        isPlaying = true
        durationMs = song.durationMs
    }

    fun togglePlayPause() {
        if (isPlaying) {
            player.pause()
            isPlaying = false
        } else {
            player.play()
            isPlaying = true
        }
    }

    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
        currentPositionMs = positionMs
    }

    fun release() {
        player.release()
    }

    fun getProgressFlow(): Flow<Float> = callbackFlow {
        while (true) {
            if (player.isPlaying) {
                currentPositionMs = player.currentPosition
                durationMs = player.duration.takeIf { it > 0 } ?: durationMs
                val progress = if (durationMs > 0) {
                    (currentPositionMs.toFloat() / durationMs).coerceIn(0f, 1f)
                } else 0f
                trySend(progress)
            }
            delay(200)
        }
        awaitClose { }
    }
}

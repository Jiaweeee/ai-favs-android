package com.example.aifavs.playback.player

import android.annotation.SuppressLint
import androidx.media3.common.ForwardingPlayer
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.aifavs.extensions.currentPlaybackPosition
import com.example.aifavs.extensions.currentTrackDuration

const val SEEK_BACK_INTERVAL_MS = 10000L
const val SEEK_FORWARD_INTERVAL_MS = 30000L

/**
 * A custom player class that provides several convenience methods for
 * controlling playback and monitoring the state of an underlying ExoPlayer.
 *
 * @param exoPlayer The ExoPlayer instance that this class wraps.
 */

@SuppressLint("UnsafeOptInUsageError")
class MyPlayer(private val exoPlayer: ExoPlayer) : ForwardingPlayer(exoPlayer) {

    override fun getAvailableCommands(): Player.Commands {
        return super.getAvailableCommands()
            .buildUpon()
            .addAll(
                Player.COMMAND_SEEK_TO_NEXT,
                Player.COMMAND_SEEK_TO_PREVIOUS
            )
            .build()
    }

    override fun play() {
        if (playbackState == Player.STATE_IDLE) prepare()
        exoPlayer.play()
    }

    override fun pause() {
        if (playbackState == Player.STATE_IDLE) prepare()
        exoPlayer.pause()
    }

    override fun seekBack() {
        val newPosition = Math.max(0, currentPlaybackPosition - SEEK_BACK_INTERVAL_MS)
        seekTo(newPosition)
    }

    override fun seekForward() {
        val newPosition = Math.min(currentTrackDuration, currentPlaybackPosition + SEEK_FORWARD_INTERVAL_MS)
        seekTo(newPosition)
    }

    override fun seekToPrevious() {
        seekBack()
    }

    override fun seekToNext() {
        seekForward()
    }
}
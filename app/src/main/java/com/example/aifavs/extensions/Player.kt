package com.example.aifavs.extensions

import androidx.media3.common.Player

inline fun <T: Player> T.runOnPlayerThread(crossinline callback: T.() -> Unit) =
    applicationLooper.post {
        callback()
    }

fun Player.setupTrack(index: Int, play: Boolean) {
    if (playbackState == Player.STATE_IDLE) prepare()
    seekTo(index, 0)
    playWhenReady = play
}

fun Player.isEnd(): Boolean = playbackState == Player.STATE_ENDED

fun Player.togglePlayback() {
    if (playbackState == Player.STATE_IDLE) prepare()
    playWhenReady = !playWhenReady
}

val Player.currentPlaybackPosition: Long
    get() = if (currentPosition > 0) currentPosition else 0L

val Player.currentTrackDuration: Long
    get() = if (duration > 0) duration else 0L

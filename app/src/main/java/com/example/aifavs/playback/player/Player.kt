package com.example.aifavs.playback.player

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import androidx.media3.common.AudioAttributes
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.example.aifavs.App
import com.example.aifavs.MainActivity

private const val PLAYER_THREAD = "PlayerThread"

@SuppressLint("UnsafeOptInUsageError")
internal fun PlaybackService.initSessionAndPlayer() {
    playerThread = HandlerThread(PLAYER_THREAD).also { it.start() }
    playerHandler = Handler(playerThread.looper)
    player = initPlayer()
    playerListener = getPlayerListener()
    mediaSession = MediaSession.Builder(this, player)
        .setSessionActivity(getSessionActivityIntent())
        .build()
    withPlayer {
        addListener(playerListener)
    }
}

@SuppressLint("UnsafeOptInUsageError")
private fun PlaybackService.initPlayer(): MyPlayer {
    return MyPlayer(
        ExoPlayer.Builder(App.context)
            .setAudioAttributes(AudioAttributes.DEFAULT, true)
            .setLooper(playerThread.looper)
            .build()
    )
}

private fun PlaybackService.getSessionActivityIntent(): PendingIntent {
    val intent = Intent(this, MainActivity::class.java)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return PendingIntent.getActivity(
        this,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
}
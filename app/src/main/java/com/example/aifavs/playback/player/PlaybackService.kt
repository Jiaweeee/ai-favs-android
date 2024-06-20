package com.example.aifavs.playback.player

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import androidx.media3.common.Player
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService


class PlaybackService : MediaSessionService() {
    internal lateinit var mediaSession: MediaSession
    internal lateinit var player: MyPlayer
    internal lateinit var playerHandler: Handler
    internal lateinit var playerThread: HandlerThread
    internal lateinit var playerListener: Player.Listener

    override fun onCreate() {
        super.onCreate()
        initSessionAndPlayer()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession.player
        if (player.playWhenReady) {
            player.pause()
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaSession()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    internal fun withPlayer(callback: MyPlayer.() -> Unit) = playerHandler.post {
        callback(player)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun releaseMediaSession() {
        mediaSession.release()
        withPlayer {
            removeListener(playerListener)
            release()
        }
    }
}
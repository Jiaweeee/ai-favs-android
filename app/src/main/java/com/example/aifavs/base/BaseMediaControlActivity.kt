package com.example.aifavs.base

import android.content.Intent
import android.os.Bundle
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.media3.session.MediaController
import androidx.viewbinding.ViewBinding
import com.example.aifavs.ALog
import com.example.aifavs.MiniPlayerView
import com.example.aifavs.extensions.isReallyPlaying
import com.example.aifavs.extensions.togglePlayback
import com.example.aifavs.playback.PlayerActivity
import com.example.aifavs.playback.player.MyMediaController

/**
 * Base class for activities that want to control the [Player].
 */
abstract class BaseMediaControlActivity<B: ViewBinding>: BaseViewBindingActivity<B>(), Player.Listener {
    private lateinit var controller: MyMediaController
    private var miniPlayer: MiniPlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller = MyMediaController.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        controller.addListener(this)
    }

    override fun onResume() {
        super.onResume()
        miniPlayer?.apply {
            withPlayer {
                updateCurrentAudio(currentMediaItem)
                updatePlayerState(isReallyPlaying)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        controller.removeListener(this)
    }

    fun setupMiniPlayer(miniPlayer: MiniPlayerView?) {
        this.miniPlayer = miniPlayer
        this.miniPlayer?.init { togglePlayback() }
        this.miniPlayer?.setOnClickListener {
            Intent(this, PlayerActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    fun withPlayer(callback: MediaController.() -> Unit) = controller.withController(callback)

    fun togglePlayback() = withPlayer { togglePlayback() }

    /**
     * Player.Listener
     */
    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        ALog.i(this.localClassName, "onPlayWhenReadyChanged")
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        ALog.i(this.localClassName, "onPlaybackStateChanged")
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        ALog.i(msg = "onIsPlayingChanged")
        miniPlayer?.apply {
            withPlayer {
                updateCurrentAudio(currentMediaItem)
            }
        }
        miniPlayer?.updatePlayerState(isPlaying)
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        if (error is ExoPlaybackException) {
            // TODO: handle error
        }
        // TODO
    }
}
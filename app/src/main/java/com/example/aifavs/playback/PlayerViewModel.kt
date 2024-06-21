package com.example.aifavs.playback

import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.media3.session.MediaController
import com.example.aifavs.App
import com.example.aifavs.extensions.currentPlaybackPosition
import com.example.aifavs.extensions.currentTrackDuration
import com.example.aifavs.extensions.isEnd
import com.example.aifavs.extensions.setupTrack
import com.example.aifavs.extensions.togglePlayback
import com.example.aifavs.playback.player.IPlayerEventsListener
import com.example.aifavs.playback.player.MyMediaController
import com.example.aifavs.playback.player.PlaybackProgress
import com.example.aifavs.playback.player.PlayerStates

class PlayerViewModel: ViewModel(), IPlayerEventsListener, Player.Listener {
    private val _playerState: MutableLiveData<PlayerStates> = MutableLiveData(PlayerStates.IDLE)
    val playerState: LiveData<PlayerStates> get() = _playerState

    private val _progress: MutableLiveData<PlaybackProgress> = MutableLiveData()
    val progress: LiveData<PlaybackProgress> get() = _progress

    private val _title: MutableLiveData<String> = MutableLiveData()
    val title: LiveData<String> get() = _title

    private val controller = MyMediaController.getInstance(App.context)
    private val handler = Handler(Looper.getMainLooper())

    init {
        controller.addListener(this)
    }

    override fun onCleared() {
        super.onCleared()
        controller.removeListener(this)
    }

    private fun withPlayer(callback: MediaController.() -> Unit) = controller.withController(callback)

    fun playAudio(url: String, title: String) {
        withPlayer {
            val mediaItem = MediaItem.Builder()
                .setUri(url)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(title)
                        .build()
                )
                .build()
            setMediaItem(mediaItem)
            setupTrack(0, true)
        }
        scheduleUIUpdate(true)
    }

    fun scheduleUIUpdate(immediate: Boolean = true, interval: Long = 1000L) {
        cancelUiUpdate()
        withPlayer {
            val delay = if (immediate) 0 else interval
            handler.postDelayed(delay) {
                updateUI(playbackState, this)
                /**
                 * TODO: Schedule UI update based on the playback state.
                 * Current implementation will update the UI even when the playback state
                 * is IDLE or END
                 */
                scheduleUIUpdate()
            }
        }
    }

    private fun cancelUiUpdate() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun updateUI(playbackState: Int? = null, player: Player) {
        val playerState = PlayerStates.fromPlaybackState(playbackState ?: player.playbackState, player)
        _playerState.postValue(playerState)
        _progress.postValue(
            PlaybackProgress(
            currentPlaybackPosition = player.currentPlaybackPosition,
            currentTrackDuration = player.currentTrackDuration
        ))
        player.currentMediaItem?.apply {
            _title.postValue(mediaMetadata.title.toString())
        }
    }

    /**
     * IPlayerEventsListener
     */
    override fun onPlayPauseClick() {
        withPlayer {
            if (isEnd()) {
                seekTo(0)
            } else {
                togglePlayback()
            }
        }
        scheduleUIUpdate()
    }

    override fun onSeekBarPositionChanged(position: Long) {
        withPlayer { seekTo(position) }
        scheduleUIUpdate()
    }

    override fun onSeekBack() {
        withPlayer { seekBack() }
        scheduleUIUpdate()
    }

    override fun onSeekForward() {
        withPlayer { seekForward() }
        scheduleUIUpdate()
    }

    /**
     * Player.Listener
     */
    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        scheduleUIUpdate()
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        scheduleUIUpdate()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        scheduleUIUpdate()
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        if (error is ExoPlaybackException) {
            // TODO: handle error
        }
        scheduleUIUpdate()
    }

}
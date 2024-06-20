package com.example.aifavs.playback

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.aifavs.R
import com.example.aifavs.base.BaseViewBindingActivity
import com.example.aifavs.databinding.ActivityPlayerBinding
import com.example.aifavs.playback.player.PlaybackProgress
import com.example.aifavs.playback.player.PlayerStates

class PlayerActivity : BaseViewBindingActivity<ActivityPlayerBinding>() {
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        val args = PlayerActivityArgs.fromBundle(intent.extras!!)
        initViews(args)
        viewModel.playerState.observe(this) { state ->
            updatePlaybackControls(state)
        }
        viewModel.progress.observe(this) {
            updateProgress(it)
        }
        viewModel.playAudio(args.audioUrl)
    }

    private fun initViews(args: PlayerActivityArgs) {
        binding.apply {
            tvTitle.text = args.title
            playbackControls.btnForward.setOnClickListener {
                viewModel.onSeekForward()
            }
            playbackControls.btnReplay.setOnClickListener {
                viewModel.onSeekBack()
            }
            playbackControls.btnPlayPause.setOnClickListener {
                viewModel.onPlayPauseClick()
            }
        }
    }

    private fun updatePlaybackControls(playerState: PlayerStates) {
        binding.playbackControls.apply {
            when(playerState) {
                PlayerStates.BUFFERING -> {
                    btnPlayPause.visibility = View.GONE
                    loadingView.visibility = View.VISIBLE
                }
                PlayerStates.PLAYING -> {
                    btnPlayPause.visibility = View.VISIBLE
                    btnPlayPause.setImageResource(R.drawable.ic_pause_circle)
                    loadingView.visibility = View.GONE
                }
                PlayerStates.PAUSE -> {
                    btnPlayPause.visibility = View.VISIBLE
                    btnPlayPause.setImageResource(R.drawable.ic_play_circle)
                    loadingView.visibility = View.GONE
                }
                else -> {
                    btnPlayPause.visibility = View.VISIBLE
                    btnPlayPause.setImageResource(R.drawable.ic_play_circle)
                    loadingView.visibility = View.GONE
                }
            }
        }
    }

    private fun updateProgress(progress: PlaybackProgress) {
        binding.apply {
            val maxProgress = progressBar.max
            if (maxProgress != progress.currentTrackDuration.toInt()) {
                progressBar.max = progress.currentTrackDuration.toInt()
            }
            progressBar.progress = progress.currentPlaybackPosition.toInt()
            val currentPosition = formatTime(progress.currentPlaybackPosition)
            tvCurrentPosition.text = currentPosition
            val duration = formatTime(progress.currentTrackDuration)
            tvDuration.text = duration
        }
    }

    private fun formatTime(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val remainingSeconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }
}
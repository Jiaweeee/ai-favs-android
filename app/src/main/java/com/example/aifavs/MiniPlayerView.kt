package com.example.aifavs

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.media3.common.MediaItem
import com.example.aifavs.databinding.LayoutMiniPlayerViewBinding
import com.example.aifavs.extensions.viewBinding

class MiniPlayerView(context: Context, attributeSet: AttributeSet) : RelativeLayout(context, attributeSet) {
    private val binding by viewBinding(LayoutMiniPlayerViewBinding::bind)

    fun init(togglePlayback: () -> Unit) {
        binding.ivPlayPause.setOnClickListener {
            togglePlayback()
        }
    }

    fun updateCurrentAudio(mediaItem: MediaItem?) {
        if (mediaItem == null) {
            hide()
            return
        }
        show()
        binding.tvTitle.text = mediaItem.mediaMetadata.title
    }

    fun updatePlayerState(isPlaying: Boolean) {
        val icon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        binding.ivPlayPause.setImageResource(icon)
    }

    private fun hide() {
        binding.root.visibility = GONE
    }

    private fun show() {
        binding.root.visibility = VISIBLE
    }
}
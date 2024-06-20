package com.example.aifavs.playback.player

import androidx.media3.common.PlaybackException
import androidx.media3.common.Player

internal fun PlaybackService.getPlayerListener() = object : Player.Listener {
    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        // TODO: post events and media for UI update
    }

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
        // TODO: post events and media for UI update
    }
}
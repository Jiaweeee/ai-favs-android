package com.example.aifavs.playback.player

import androidx.media3.common.Player

/**
 * An enumeration of the possible states for the player.
 */
enum class PlayerStates {
    /**
     * State when the player is idle, not ready to play.
     */
    IDLE,

    /**
     * State when the player is ready to start playback.
     */
    READY,

    /**
     * State when the player is buffering content.
     */
    BUFFERING,

    /**
     * State when the player has encountered an error.
     */
    ERROR,

    /**
     * State when the playback has ended.
     */
    END,

    /**
     * State when the player is actively playing content.
     */
    PLAYING,

    /**
     * State when the player has paused the playback.
     */
    PAUSE,


    UNKNOWN;

    companion object {
        fun fromPlaybackState(state: Int, player: Player?): PlayerStates {
            return when (state) {
                Player.STATE_IDLE -> IDLE
                Player.STATE_BUFFERING -> BUFFERING
                Player.STATE_READY -> if (player?.playWhenReady == true) PLAYING else PAUSE
                Player.STATE_ENDED -> END
                else -> UNKNOWN
            }
        }
    }
}

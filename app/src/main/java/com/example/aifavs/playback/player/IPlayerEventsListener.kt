package com.example.aifavs.playback.player

/**
 * An interface for handling player events such as play, pause, next, previous, and seek bar position changes.
 */
interface IPlayerEventsListener {

    /**
     * Invoked when the play or pause button is clicked.
     */
    fun onPlayPauseClick()

    /**
     * Invoked when the position of the seek bar has changed. The new position is provided as a parameter.
     *
     * @param position The new position of the seek bar.
     */
    fun onSeekBarPositionChanged(position: Long)

    fun onSeekBack()

    fun onSeekForward()
}

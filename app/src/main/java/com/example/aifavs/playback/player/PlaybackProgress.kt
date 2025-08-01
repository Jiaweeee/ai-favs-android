package com.example.aifavs.playback.player

/**
 * Data class that represents the current playback state of a media item.
 *
 * @property currentPlaybackPosition Current position in the media item that's currently playing, in milliseconds.
 * @property currentTrackDuration Duration of the current track that's playing, in milliseconds.
 */
data class PlaybackProgress(
    val currentPlaybackPosition: Long,
    val currentTrackDuration: Long
) {

}
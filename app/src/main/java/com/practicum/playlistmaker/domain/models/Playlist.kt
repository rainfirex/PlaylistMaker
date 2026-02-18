package com.practicum.playlistmaker.domain.models

data class Playlist(
    val id: Int,
    val namePlaylist: String,
    val description: String?,
    val pathImage: String?,
    val tracks: String?,
    val count: Int
)

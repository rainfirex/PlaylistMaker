package com.practicum.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Int,
    val namePlaylist: String,
    val description: String?,
    val pathImage: String?,
    val tracks: String?,
    val count: Int
): Parcelable

package com.practicum.playlistmaker.ui.common.models

import com.practicum.playlistmaker.domain.models.Playlist

sealed interface PlaylistsState {
    data class Playlists(val playLists: List<Playlist>): PlaylistsState
}
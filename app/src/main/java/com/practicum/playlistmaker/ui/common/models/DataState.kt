package com.practicum.playlistmaker.ui.common.models

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.models.Playlist

sealed interface DataState{
    data class DataTracks(val tracks: List<Track>): DataState
    data class DataPlaylist(val playlist: Playlist): DataState
    data class DataPlaylists(val playlists: List<Playlist>): DataState
}
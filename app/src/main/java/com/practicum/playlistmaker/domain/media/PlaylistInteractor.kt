package com.practicum.playlistmaker.domain.media

import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun addTrack(track: Track, playlist: Playlist)
}
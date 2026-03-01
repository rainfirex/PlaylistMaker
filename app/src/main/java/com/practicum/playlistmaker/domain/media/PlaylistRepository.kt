package com.practicum.playlistmaker.domain.media

import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun insertPlaylist(playlist: Playlist): Long

    suspend fun addTrack(track: Track, playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    fun getPlaylist(id: Int): Flow<Playlist>

    fun getTracks(ids: List<Int>): Flow<List<Track>>

    suspend fun updateTracks(playlistId: Int, tracks: String, count: Int): Flow<Int>

    suspend fun removeTrack(trackId: Int)

    suspend fun removePlaylist(playlist: Playlist): Flow<Int>

    suspend fun updatePlaylist(playlist: Playlist)
}
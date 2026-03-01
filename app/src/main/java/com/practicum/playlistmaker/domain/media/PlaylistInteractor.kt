package com.practicum.playlistmaker.domain.media

import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun getTracks(tracks: String): Flow<List<Track>>

    fun getPlaylist(id: Int) : Flow<Playlist>

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun addTrack(track: Track, playlist: Playlist)

    suspend fun updateTracks(playlist: Playlist, trackId: Int): Flow<Int>

    suspend fun removeTrack(trackId: Int)

    suspend fun removePlaylist(playlist: Playlist): Flow<Int>

    suspend fun removeTracks(playlistTracks: String)

    suspend fun updatePlaylist(playlist: Playlist, namePlaylist: String, description: String, pathImage: String?)
}
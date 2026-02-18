package com.practicum.playlistmaker.domain.media.impl

import com.practicum.playlistmaker.domain.media.PlaylistInteractor
import com.practicum.playlistmaker.domain.media.PlaylistRepository
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {

    override fun getPlaylists(): Flow<List<Playlist>>{
        return playlistRepository.getPlaylists()
    }

    override suspend fun insertPlaylist(playlist: Playlist){
        playlistRepository.insertPlaylist(playlist)
    }

    override suspend fun addTrack(track: Track, playlist: Playlist) {
        playlistRepository.addTrack(track, playlist)
    }
}
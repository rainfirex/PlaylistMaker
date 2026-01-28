package com.practicum.playlistmaker.domain.media.impl

import com.practicum.playlistmaker.domain.media.MediaInteractor
import com.practicum.playlistmaker.domain.media.MediaRepository
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class MediaInteractorImpl(private val mediaRepository: MediaRepository): MediaInteractor {

    override fun getTracks(): Flow<List<Track>>{
        return mediaRepository.getTracks()
    }

    override suspend fun insertTrack(track: Track){
        mediaRepository.insertTrack(track)
    }

    override suspend fun removeTrack(track: Track){
        mediaRepository.removeTrack(track)
    }
}
package com.practicum.playlistmaker.domain.media

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaInteractor {
    fun getTracks(): Flow<List<Track>>
    suspend fun insertTrack(track: Track)
    suspend fun removeTrack(track: Track)
}
package com.practicum.playlistmaker.domain.media

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    suspend fun insertTrack(track: Track)
    suspend fun removeTrack(track: Track)
    fun getTracks(): Flow<List<Track>>
}
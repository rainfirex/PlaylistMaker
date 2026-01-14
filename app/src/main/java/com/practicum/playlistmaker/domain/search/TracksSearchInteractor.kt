package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksSearchInteractor {
    fun searchTracks(expression: String): Flow<Result<List<Track>>>
}
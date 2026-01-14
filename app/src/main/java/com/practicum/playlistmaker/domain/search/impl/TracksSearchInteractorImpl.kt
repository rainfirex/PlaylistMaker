package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.TracksSearchInteractor
import com.practicum.playlistmaker.domain.search.TracksSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlin.Result

class TracksSearchInteractorImpl(private val trackSearchRepository: TracksSearchRepository): TracksSearchInteractor {

    override fun searchTracks(expression: String): Flow<Result<List<Track>>> {
        return trackSearchRepository.searchTracks(expression)
    }
}
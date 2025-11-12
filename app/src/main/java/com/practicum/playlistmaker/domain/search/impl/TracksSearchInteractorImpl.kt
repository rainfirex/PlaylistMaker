package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.TracksSearchInteractor
import com.practicum.playlistmaker.domain.search.TracksSearchRepository
import com.practicum.playlistmaker.domain.search.consumer.TrackSearchResult
import java.util.concurrent.Executors

class TracksSearchInteractorImpl(private val trackSearchRepository: TracksSearchRepository): TracksSearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TrackSearchResult<List<Track>>) {
        executor.execute {
            val tracks = trackSearchRepository.searchTracks(expression)
            consumer.consume(Result.success(tracks))
        }
    }
}
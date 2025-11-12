package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.consumer.TrackSearchResult

interface TracksSearchInteractor {
    fun searchTracks(expression: String, consumer: TrackSearchResult<List<Track>>)
}
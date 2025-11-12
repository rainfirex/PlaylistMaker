package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track

interface TracksSearchRepository {
    fun searchTracks(expression: String): List<Track>
}
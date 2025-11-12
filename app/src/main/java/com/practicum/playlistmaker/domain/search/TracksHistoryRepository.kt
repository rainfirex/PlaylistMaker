package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track

interface TracksHistoryRepository {
    fun load(): MutableList<Track>
    fun save(tracks: MutableList<Track>)
}
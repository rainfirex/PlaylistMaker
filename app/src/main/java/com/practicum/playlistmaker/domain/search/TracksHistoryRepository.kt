package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.data.search.dto.TrackDto

interface TracksHistoryRepository {
    fun load(): MutableList<TrackDto>
    fun save(tracks: MutableList<TrackDto>)
}
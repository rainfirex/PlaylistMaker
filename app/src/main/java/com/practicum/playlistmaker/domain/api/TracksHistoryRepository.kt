package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.data.dto.TrackDto

interface TracksHistoryRepository {
    fun load(): MutableList<TrackDto>
    fun save(tracks: MutableList<TrackDto>)
}
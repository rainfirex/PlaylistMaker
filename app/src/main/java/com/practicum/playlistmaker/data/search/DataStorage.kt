package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.search.dto.TrackDto

interface DataStorage {
    fun save(tracks: MutableList<TrackDto>)
    fun load(): MutableList<TrackDto>
}
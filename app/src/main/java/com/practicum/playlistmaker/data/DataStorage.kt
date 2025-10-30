package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackDto

interface DataStorage {
    fun save(tracks: MutableList<TrackDto>)
    fun load(): MutableList<TrackDto>
}
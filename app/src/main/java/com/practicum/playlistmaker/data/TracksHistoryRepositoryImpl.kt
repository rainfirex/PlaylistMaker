package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.api.TracksHistoryRepository

class TracksHistoryRepositoryImpl(private val dataStorage: DataStorage): TracksHistoryRepository {
    override fun load(): MutableList<TrackDto> {
        return dataStorage.load()
    }

    override fun save(tracks: MutableList<TrackDto>) {
       dataStorage.save(tracks)
    }
}
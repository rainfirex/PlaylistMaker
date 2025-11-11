package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.search.DataStorage
import com.practicum.playlistmaker.data.search.dto.TrackDto
import com.practicum.playlistmaker.domain.search.TracksHistoryRepository

class TracksHistoryRepositoryImpl(private val dataStorage: DataStorage): TracksHistoryRepository {
    override fun load(): MutableList<TrackDto> {
        return dataStorage.load()
    }

    override fun save(tracks: MutableList<TrackDto>) {
       dataStorage.save(tracks)
    }
}
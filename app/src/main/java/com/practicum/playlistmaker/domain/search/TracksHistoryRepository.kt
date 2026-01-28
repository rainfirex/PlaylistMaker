package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksHistoryRepository {
    fun load(): Flow<MutableList<Track>>
    fun save(tracks: MutableList<Track>)
}
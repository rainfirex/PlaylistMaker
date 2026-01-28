package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksHistoryInteractor {
    fun addTrack(track: Track, i: Int)
    fun loadTracks(): Flow<MutableList<Track>>
    fun save()
    fun clear()
    fun count(): Int
    fun getTracks(): MutableList<Track>
    fun setTracks(tracks: MutableList<Track>)
}
package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.models.Track

interface TracksHistoryInteractor {
    fun addTrack(track: Track, i: Int)
    fun loadTracks(): MutableList<Track>
    fun save()
    fun clear()
    fun count(): Int
    fun getTracks(): MutableList<Track>
}
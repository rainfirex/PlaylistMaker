package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TracksHistoryInter {
    fun addTrack(track: Track, i: Int)
    fun getTracks(): MutableList<Track>
    fun save()
    fun clear()
}
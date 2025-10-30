package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.consumer.TrackSearchConsumer
import com.practicum.playlistmaker.domain.models.Track

interface TracksSearchInter {
    fun searchTracks(expression: String, consumer: TrackSearchConsumer<List<Track>>)

    fun getTracks(): MutableList<Track>

    fun setTracks(tracks: List<Track>)

    fun clear()
}
package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.search.consumer.TrackSearchConsumer
import com.practicum.playlistmaker.domain.models.Track

interface TracksSearchInter {
    fun searchTracks(expression: String, consumer: TrackSearchConsumer<MutableList<Track>>)
}
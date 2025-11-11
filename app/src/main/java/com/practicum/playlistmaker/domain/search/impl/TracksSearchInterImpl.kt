package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.search.consumer.ConsumerData
import com.practicum.playlistmaker.domain.search.consumer.TrackSearchConsumer
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.TracksSearchInter
import com.practicum.playlistmaker.domain.search.TracksSearchRepository
import java.util.concurrent.Executors

class TracksSearchInterImpl(private val trackSearchRepository: TracksSearchRepository): TracksSearchInter {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TrackSearchConsumer<MutableList<Track>>) {
        executor.execute {
            //resource
            val tracks = trackSearchRepository.searchTracks(expression)
            consumer.consume(ConsumerData.Data(tracks))
        }
    }
}
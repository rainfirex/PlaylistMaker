package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.TracksSearchInter
import com.practicum.playlistmaker.domain.api.TracksSearchRepository
import com.practicum.playlistmaker.domain.consumer.ConsumerData
import com.practicum.playlistmaker.domain.consumer.TrackSearchConsumer
import com.practicum.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TracksSearchInterImpl(private val trackSearchRepository: TracksSearchRepository):  TracksSearchInter{

    private val executor = Executors.newCachedThreadPool()

    private val tracks = mutableListOf<Track>()

    override fun searchTracks(expression: String, consumer: TrackSearchConsumer<List<Track>>) {
        executor.execute {
            val tracks = trackSearchRepository.searchTracks(expression)
            consumer.consume(ConsumerData.Data(tracks))
        }
    }

    override fun getTracks(): MutableList<Track>{
        return tracks
    }

    override fun setTracks(trackList: List<Track>) {
        tracks.addAll(trackList)
    }

    override fun clear() {
        tracks.clear()
    }
}
package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.db.Database
import com.practicum.playlistmaker.data.search.DataStorage
import com.practicum.playlistmaker.data.search.dto.TrackDto
import com.practicum.playlistmaker.data.search.map.SearchMapper
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.TracksHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksHistoryRepositoryImpl(private val dataStorage: DataStorage, private val database: Database, private val mapper: SearchMapper): TracksHistoryRepository {

    override fun load(): Flow<MutableList<Track>> = flow{
        val ids = database.trackDao().getIds()
        val tracksDto = dataStorage.load()
        val tracks = mapping(tracksDto, ids)
        emit(tracks)
    }

    override fun save(tracks: MutableList<Track>) {
        val tracksDto = mapping(tracks)
        dataStorage.save(tracksDto)
    }

    private fun mapping(tracks: List<TrackDto>, ids: List<Int>): MutableList<Track>{
        return tracks.map { track ->
            val existFavorite = (track.trackId in ids)
            val t = mapper.map(track)
            t.isFavorite = existFavorite
            t
        } as MutableList<Track>
    }

    private fun mapping(tracks: List<Track>): MutableList<TrackDto>{
        return tracks.map { track ->
            mapper.map(track)
        } as MutableList<TrackDto>
    }
}
package com.practicum.playlistmaker.data.media.impl

import com.practicum.playlistmaker.data.media.db.Database
import com.practicum.playlistmaker.data.media.dto.TrackMedia
import com.practicum.playlistmaker.data.media.map.MediaMapper
import com.practicum.playlistmaker.domain.media.MediaRepository
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaRepositoryImpl(private val db :Database, private val mapper: MediaMapper) : MediaRepository{

    override suspend fun insertTrack(track: Track){
        val trackDto = mapper.map(track)
        db.trackDao().insert(trackDto)
    }

    override suspend fun removeTrack(track: Track) {
        val trackDto = mapper.map(track)
        db.trackDao().remove(trackDto)
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = db.trackDao().getTracks()
        emit(mapping(tracks))
    }

    private fun mapping(tracks: List<TrackMedia>): List<Track>{
        return tracks.map { track ->
            mapper.map(track)
        }
    }

}
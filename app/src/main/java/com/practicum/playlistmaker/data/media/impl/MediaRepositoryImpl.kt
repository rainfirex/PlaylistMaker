package com.practicum.playlistmaker.data.media.impl

import com.practicum.playlistmaker.data.db.Database
import com.practicum.playlistmaker.data.media.dto.TrackMedia
import com.practicum.playlistmaker.data.media.map.MediaMapper
import com.practicum.playlistmaker.domain.media.MediaRepository
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

class MediaRepositoryImpl(private val db :Database, private val mapper: MediaMapper) : MediaRepository{

    override suspend fun insertTrack(track: Track){
        val trackDto = mapper.map(track)
        trackDto.created = Date()
        db.trackDao().insert(trackDto)
    }

    override suspend fun removeTrack(track: Track) {
        val trackDto = mapper.map(track)
        db.trackDao().remove(trackDto)
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val ids = db.trackDao().getIds()
        val tracks = db.trackDao().getTracks()
        emit(mapping(tracks, ids))
    }

    private fun mapping(tracks: List<TrackMedia>, ids: List<Int>): List<Track>{
        return tracks.map { track ->
            val existFavorite = (track.id in ids)
            val t = mapper.map(track)
            t.copy(isFavorite = existFavorite)
//            t.isFavorite = existFavorite
//            t
        }
    }

}
package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.media.db.Database
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.dto.TrackDto
import com.practicum.playlistmaker.data.search.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.search.dto.TracksSearchResponse
import com.practicum.playlistmaker.data.search.map.SearchMapper
import com.practicum.playlistmaker.domain.search.TracksSearchRepository
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackSearchRepositoryImpl(private val networkClient: NetworkClient, private val database: Database, private val mapper: SearchMapper) : TracksSearchRepository {

    override fun searchTracks(expression: String): Flow<Result<List<Track>>> = flow{
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        if(response.resultCode == 200){
            val ids = database.trackDao().getIds()
            val listDto = (response as TracksSearchResponse).results
            emit(Result.success(mapping(listDto, ids)))
        }
        else{
            emit(Result.success(listOf()))
        }
    }

    private fun mapping(tracks: List<TrackDto>, ids: List<Int>): List<Track>{
        return tracks.map { track ->
            val existFavorite = (track.trackId in ids)
            val t = mapper.map(track)
            t.isFavorite = existFavorite
            t
        }
    }
}
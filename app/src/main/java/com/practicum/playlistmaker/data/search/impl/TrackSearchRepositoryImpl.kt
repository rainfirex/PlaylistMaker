package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.media.db.Database
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.search.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.search.TracksSearchRepository
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackSearchRepositoryImpl(private val networkClient: NetworkClient, private val database: Database) : TracksSearchRepository {

    override fun searchTracks(expression: String): Flow<Result<List<Track>>> = flow{
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        if(response.resultCode == 200){

            val ids = database.trackDao().getIds()

            val data = (response as TracksSearchResponse).results.map { trackDto ->
                val existFavorite = (trackDto.trackId in ids)

                Track(trackDto.trackId, trackDto.trackName, trackDto.artistName, trackDto.trackTimeMillis,
                    trackDto.artworkUrl100, trackDto.collectionName, trackDto.releaseDate ?: "",
                    trackDto.primaryGenreName, trackDto.country, trackDto.previewUrl ?: "", existFavorite)
            }

            emit(Result.success(data))
        }
        else{
            emit(Result.success(listOf()))
        }
    }
}
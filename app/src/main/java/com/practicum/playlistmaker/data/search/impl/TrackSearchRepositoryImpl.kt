package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.search.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.search.TracksSearchRepository
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackSearchRepositoryImpl(private val networkClient: NetworkClient) : TracksSearchRepository {

    override fun searchTracks(expression: String): Flow<Result<List<Track>>> = flow{
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        if(response.resultCode == 200){
            val data = (response as TracksSearchResponse).results.map {
                Track(it.trackId, it.trackName, it.artistName, it.trackTimeMillis,
                    it.artworkUrl100, it.collectionName, it.releaseDate ?: "",
                    it.primaryGenreName, it.country, it.previewUrl ?: "" )
            }

            emit(Result.success(data))
        }
        else{
            emit(Result.success(listOf()))
        }
    }
}
package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.api.TracksSearchRepository
import com.practicum.playlistmaker.domain.models.Track

class TrackSearchRepositoryImpl(private val networkClient: NetworkClient) : TracksSearchRepository {

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return if(response.resultCode == 200){
            (response as TracksSearchResponse).results.map {
                Track(it.trackId, it.trackName, it.artistName, it.trackTimeMillis,
                    it.artworkUrl100, it.collectionName, it.releaseDate ?: "",
                    it.primaryGenreName, it.country, it.previewUrl ?: "" )
            }
        } else{
            emptyList()
        }
    }
}
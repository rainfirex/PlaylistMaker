package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.search.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.search.TracksSearchRepository
import com.practicum.playlistmaker.domain.models.Track

class TrackSearchRepositoryImpl(private val networkClient: NetworkClient) : TracksSearchRepository {

    override fun searchTracks(expression: String): MutableList<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return (if(response.resultCode == 200){
            (response as TracksSearchResponse).results.map {
                Track(it.trackId, it.trackName, it.artistName, it.trackTimeMillis,
                    it.artworkUrl100, it.collectionName, it.releaseDate ?: "",
                    it.primaryGenreName, it.country, it.previewUrl ?: "" )
            }
        }
        else{
            mutableListOf()
        }) as MutableList<Track>
    }
}
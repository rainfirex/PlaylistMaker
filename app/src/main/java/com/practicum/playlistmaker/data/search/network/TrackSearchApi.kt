package com.practicum.playlistmaker.data.search.network

import com.practicum.playlistmaker.data.search.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackSearchApi {
    @GET("/search?entity=song")
    suspend fun getTracks(@Query("term") song: String): TracksSearchResponse
}
package com.practicum.playlistmaker.api

import com.practicum.playlistmaker.responses.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackSearchApi {
    @GET("/search")
    fun getTracks(@Query("term") song: String): Call<TrackSearchResponse>
}
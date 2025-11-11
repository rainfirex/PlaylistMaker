package com.practicum.playlistmaker.data.search.network

import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.dto.Response
import com.practicum.playlistmaker.data.search.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    private val url = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceSearchApi = retrofit.create(TrackSearchApi::class.java)

    override fun doRequest(dtoRequest: Any): Response {
        if(dtoRequest is TrackSearchRequest){
            try {
                val response = serviceSearchApi.getTracks(dtoRequest.expression).execute()
                val body = response.body() ?: Response()

                return body.apply { resultCode = response.code() }
            }
            catch (ex: Exception){
                return Response().apply { resultCode = -1 }
            }
        }
        else{
            return Response().apply { resultCode = 400 }
        }
    }
}
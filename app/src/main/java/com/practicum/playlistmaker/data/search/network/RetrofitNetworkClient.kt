package com.practicum.playlistmaker.data.search.network

import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.dto.Response
import com.practicum.playlistmaker.data.search.dto.TrackSearchRequest

class RetrofitNetworkClient(private val serviceSearchApi: TrackSearchApi): NetworkClient {

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
package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.search.dto.Response

interface NetworkClient {
    fun doRequest(dtoRequest: Any): Response
}
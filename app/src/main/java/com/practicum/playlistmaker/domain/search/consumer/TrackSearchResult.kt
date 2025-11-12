package com.practicum.playlistmaker.domain.search.consumer

interface TrackSearchResult<T> {
    fun consume(data: Result<T>)
}
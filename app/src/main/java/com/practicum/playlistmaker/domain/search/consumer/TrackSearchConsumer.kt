package com.practicum.playlistmaker.domain.search.consumer

interface TrackSearchConsumer<T> {
    fun consume(data: ConsumerData<T>)
}
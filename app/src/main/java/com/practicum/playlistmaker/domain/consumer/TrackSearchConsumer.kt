package com.practicum.playlistmaker.domain.consumer

interface TrackSearchConsumer<T> {
    fun consume(data: ConsumerData<T>)
}
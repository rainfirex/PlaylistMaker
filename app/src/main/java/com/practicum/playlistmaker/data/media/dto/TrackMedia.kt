package com.practicum.playlistmaker.data.media.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track")
data class TrackMedia(
    @PrimaryKey
    val id: Int,
    val trackName: String,
    val artistName: String,
    val collectionName:  String,
    val releaseDate: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val previewUrl: String,
    val primaryGenreName: String,
    val country: String
)
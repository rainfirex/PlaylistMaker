package com.practicum.playlistmaker.data.media.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "track_playlist")
data class TrackPlaylist(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "track_name") val trackName: String,
    @ColumnInfo(name = "artist_name") val artistName: String,
    @ColumnInfo(name = "collection_name") val collectionName:  String,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    @ColumnInfo(name = "track_time_millis") val trackTimeMillis: Int,
    @ColumnInfo(name = "artwork_url") val artworkUrl100: String,
    @ColumnInfo(name = "preview_url") val previewUrl: String,
    @ColumnInfo(name = "primary_genre_name") val primaryGenreName: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    var created: Date? = null
)

package com.practicum.playlistmaker.data.media.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "entity")
data class Entity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name_playlist") val namePlaylist: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "path_image") val pathImage: String?,
    @ColumnInfo(name = "tracks") val tracks: String?,
    @ColumnInfo(name = "count_tracks", defaultValue = "0") val count: Int,
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    var created: Date? = null,
    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    var updated: Date? = null,
)

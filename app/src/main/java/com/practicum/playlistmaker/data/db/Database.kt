package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practicum.playlistmaker.data.media.dao.EntityDao
import com.practicum.playlistmaker.data.media.dao.TrackMediaDao
import com.practicum.playlistmaker.data.media.dao.TrackPlaylistDao
import com.practicum.playlistmaker.data.media.dto.Entity
import com.practicum.playlistmaker.data.media.dto.TrackMedia
import com.practicum.playlistmaker.data.media.dto.TrackPlaylist
import com.practicum.playlistmaker.data.media.map.ConverterLocalDateTime

@Database(
    version = 1,
    entities = [
        TrackMedia::class,
        Entity::class,
        TrackPlaylist::class
    ]
)
@TypeConverters(ConverterLocalDateTime::class)
abstract class Database: RoomDatabase() {
    abstract fun trackDao(): TrackMediaDao
    abstract fun entityDao(): EntityDao
    abstract fun trackPlaylistDao(): TrackPlaylistDao
}
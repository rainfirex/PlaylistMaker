package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practicum.playlistmaker.data.media.dao.TrackMediaDao
import com.practicum.playlistmaker.data.media.dto.TrackMedia
import com.practicum.playlistmaker.data.media.map.ConverterLocalDateTime

@Database(
    version = 1,
    entities = [
        TrackMedia::class
    ]
)
@TypeConverters(ConverterLocalDateTime::class)
abstract class Database: RoomDatabase() {
    abstract fun trackDao(): TrackMediaDao
}
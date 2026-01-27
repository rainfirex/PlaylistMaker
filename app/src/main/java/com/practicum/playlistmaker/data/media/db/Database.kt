package com.practicum.playlistmaker.data.media.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.media.dao.TrackMediaDao
import com.practicum.playlistmaker.data.media.dto.TrackMedia

@Database(
    version = 1,
    entities = [
        TrackMedia::class
    ]
)
abstract class Database: RoomDatabase() {

    abstract fun trackDao(): TrackMediaDao

}
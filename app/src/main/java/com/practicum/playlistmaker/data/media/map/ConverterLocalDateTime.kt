package com.practicum.playlistmaker.data.media.map

import androidx.room.TypeConverter
import java.util.Date

class ConverterLocalDateTime {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}
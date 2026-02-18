package com.practicum.playlistmaker.data.media.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.media.dto.Entity

@Dao
interface EntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Entity): Long

    @Query("SELECT * FROM entity ORDER BY created_at DESC")
    suspend fun getPlaylists(): List<Entity>
}
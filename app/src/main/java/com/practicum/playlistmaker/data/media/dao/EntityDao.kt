package com.practicum.playlistmaker.data.media.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker.data.media.dto.Entity

@Dao
interface EntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Entity): Long

    @Query("SELECT * FROM entity ORDER BY updated_at DESC")
    suspend fun getPlaylists(): List<Entity>

    @Query("SELECT * FROM entity WHERE id = :id")
    suspend fun getPlaylist(id: Int): Entity

    @Query("UPDATE entity SET tracks = :tracks, count_tracks = :count WHERE id = :playlistId")
    suspend fun updateTracks(playlistId: Int, tracks: String, count: Int): Int

    @Query("SELECT COUNT(*) FROM entity WHERE tracks LIKE '%' || :trackId || '%'")
    suspend fun isUsedTrack(trackId: Int): Int

    @Delete
    suspend fun removePlaylist(playlist: Entity): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlist: Entity)
}
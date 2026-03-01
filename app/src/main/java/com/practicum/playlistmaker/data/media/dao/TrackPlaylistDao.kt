package com.practicum.playlistmaker.data.media.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.media.dto.TrackPlaylist

@Dao
interface TrackPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackPlaylist): Long

    @Query("SELECT * FROM track_playlist WHERE id iN(:ids) ORDER BY created_at DESC")
    suspend fun getTracks(ids: List<Int>): List<TrackPlaylist>

    @Query("DELETE FROM track_playlist WHERE id = :trackId")
    suspend fun removeTrack(trackId: Int)
}
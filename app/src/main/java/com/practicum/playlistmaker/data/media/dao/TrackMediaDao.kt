package com.practicum.playlistmaker.data.media.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.media.dto.TrackMedia

@Dao
interface TrackMediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackMedia>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: TrackMedia)

    @Delete
    suspend fun remove(track: TrackMedia)

    @Query("SELECT * FROM track ORDER BY id") //DESC
    suspend fun getTracks(): List<TrackMedia>

    @Query("SELECT id FROM track")
    suspend fun getIds(): List<Int>
}
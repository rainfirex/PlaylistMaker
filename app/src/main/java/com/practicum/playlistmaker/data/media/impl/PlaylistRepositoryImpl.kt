package com.practicum.playlistmaker.data.media.impl

import com.practicum.playlistmaker.data.db.Database
import com.practicum.playlistmaker.data.media.map.MediaMapper
import com.practicum.playlistmaker.domain.media.PlaylistRepository
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

class PlaylistRepositoryImpl(private val db :Database, private val mapper: MediaMapper): PlaylistRepository {

    suspend fun insertTrack(track: Track): Long{
        val tr = mapper.mapTrackPlaylist(track)
        tr.created = Date()
        return db.trackPlaylistDao().insertTrack(tr)
    }

    override suspend fun insertPlaylist(playlist: Playlist): Long{
        val entityDto = mapper.map(playlist)
        entityDto.created = Date()
        return db.entityDao().insertPlaylist(entityDto)
    }

    override suspend fun addTrack(track: Track, playlist: Playlist) {
        val tracks = if(playlist.tracks == null){
             "${track.trackId};"
        }
        else {
            "${playlist.tracks}${track.trackId};"
        }

        val countTrack = playlist.count + 1
        val playlistUpdated = playlist.copy(tracks = tracks, count = countTrack)

        insertTrack(track)
        insertPlaylist(playlistUpdated)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val entityDto = db.entityDao().getPlaylists()
        val playlists = entityDto.map {
            mapper.map(it)
        }
        emit(playlists)
    }
}
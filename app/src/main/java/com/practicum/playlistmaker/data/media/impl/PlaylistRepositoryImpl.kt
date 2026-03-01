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
        val tr = mapper.trackToTrackPlaylist(track)
        tr.created = Date()
        return db.trackPlaylistDao().insertTrack(tr)
    }

    override suspend fun insertPlaylist(playlist: Playlist): Long{
        val entityDto = mapper.map(playlist)
        entityDto.created = Date()
        entityDto.updated = Date()
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

    override fun getPlaylist(id: Int): Flow<Playlist> = flow {
        val entityDto = db.entityDao().getPlaylist(id)
        val playlist = mapper.map(entityDto)
        emit(playlist)
    }

    override fun getTracks(ids: List<Int>): Flow<List<Track>> = flow{
        val tracksDto = db.trackPlaylistDao().getTracks(ids)
        val tracks = tracksDto.map {
            mapper.trackPlaylistToTrack(it)
        }
        val sortedTracks = ids.mapNotNull{ id ->
            tracks.find{ it.trackId == id}
        }
        emit(sortedTracks)
    }

    override suspend fun updateTracks(playlistId: Int, tracks: String, count: Int): Flow<Int> = flow{
        val rowUpdate = db.entityDao().updateTracks(playlistId, tracks, count)
        emit(rowUpdate)
    }

    override suspend fun removeTrack(trackId: Int) {
        val countUsed = db.entityDao().isUsedTrack(trackId)
        if(countUsed == 0){
            db.trackPlaylistDao().removeTrack(trackId)
        }
    }

    override suspend fun removePlaylist(playlist: Playlist) : Flow<Int> = flow{
        val entityDto = mapper.map(playlist)
        val rowUpdate = db.entityDao().removePlaylist(entityDto)
        emit(rowUpdate)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val entityDto = mapper.map(playlist)
        entityDto.updated = Date()
        db.entityDao().updatePlaylist(entityDto)
    }
}
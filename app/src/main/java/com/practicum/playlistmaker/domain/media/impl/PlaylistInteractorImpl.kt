package com.practicum.playlistmaker.domain.media.impl

import com.practicum.playlistmaker.domain.media.PlaylistInteractor
import com.practicum.playlistmaker.domain.media.PlaylistRepository
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlin.text.isNotEmpty
import kotlin.text.split

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {

    override fun getTracks(tracks: String): Flow<List<Track>> {
        val ids = tracks.split(DELIMITER)
        val tracks = ids.filter { it.isNotEmpty() }
            .filterNot { it == NO_UPDATE.toString() }
            .mapNotNull { it.toIntOrNull() }
            .reversed()

        return playlistRepository.getTracks(tracks)
    }

    override fun getPlaylist(id: Int): Flow<Playlist> {
        return playlistRepository.getPlaylist(id)
    }

    override fun getPlaylists(): Flow<List<Playlist>>{
        return playlistRepository.getPlaylists()
    }

    override suspend fun insertPlaylist(playlist: Playlist){
        playlistRepository.insertPlaylist(playlist)
    }

    override suspend fun addTrack(track: Track, playlist: Playlist) {
        playlistRepository.addTrack(track, playlist)
    }

    override suspend fun updateTracks(playlist: Playlist, trackId: Int): Flow<Int> {
        if(playlist.tracks != null){
            val ids = playlist.tracks.split(DELIMITER)
            val list = ids.filter { it.isNotEmpty() }
                .filterNot { it == trackId.toString() }
                .filterNot { it == "-1" }

            val tracks = list.joinToString(separator = DELIMITER)

            val joinedTracks = "${tracks};"

            return playlistRepository.updateTracks(playlist.id, joinedTracks, list.size)
        }
        return flow { NO_UPDATE }
    }

    override suspend fun removeTrack(trackId: Int) {
        playlistRepository.removeTrack(trackId)
    }

    override suspend fun removeTracks(playlistTracks: String){
        if(playlistTracks.isNotEmpty()){
            val ids = playlistTracks.split(DELIMITER)
            var list = ids
                .filter { it.isNotEmpty() }
                .filterNot { it == NO_UPDATE.toString() }
                .mapNotNull { it.toIntOrNull() }


            list.forEach { trackId ->
                removeTrack(trackId)
            }
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist, namePlaylist: String, description: String, pathImage: String?) {
        var path = pathImage
        if(path == null){
            path = playlist.pathImage
        }

        val pl = playlist.copy(namePlaylist = namePlaylist, description = description, pathImage = path)

        playlistRepository.updatePlaylist(pl)
    }

    override suspend fun removePlaylist(playlist: Playlist): Flow<Int> {
        val playlistTracks = playlist.tracks
        return playlistRepository.removePlaylist(playlist).onEach { row ->
            if(playlistTracks != null){
                removeTracks(playlistTracks)
            }
        }
    }

    companion object{
        private const val DELIMITER = ";"
        private const val NO_UPDATE = -1
    }
}
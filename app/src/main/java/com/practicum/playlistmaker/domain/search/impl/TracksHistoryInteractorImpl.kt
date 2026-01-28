package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.TracksHistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksHistoryRepository
import kotlinx.coroutines.flow.Flow

class TracksHistoryInteractorImpl(private val repository: TracksHistoryRepository): TracksHistoryInteractor {

    private var tracks = mutableListOf<Track>()

    override fun addTrack(track: Track, i: Int) {

        val existTrack = tracks.firstOrNull { it -> it.trackId == track.trackId }
        if(existTrack != null){
            tracks.remove(existTrack)
        }
        else if(tracks.size == 10) {
            val lastPosition = tracks.size-1
            tracks.removeAt(lastPosition)
        }

        val indexInsert = 0
        tracks.add(indexInsert, track)
    }

    override fun setTracks(tracks: MutableList<Track>){
        this.tracks = tracks
    }

    override fun loadTracks(): Flow<MutableList<Track>>{
        return repository.load()
    }

    override fun getTracks(): MutableList<Track>{
        return tracks
    }

    override fun save() {
        repository.save(tracks)
    }

    override fun clear() {
        tracks.clear()
    }

    override fun count(): Int{
        return tracks.size
    }
}
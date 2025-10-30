package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.api.TracksHistoryInter
import com.practicum.playlistmaker.domain.api.TracksHistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class TracksHistoryInterImpl(private val repository: TracksHistoryRepository): TracksHistoryInter{

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

    override fun getTracks(): MutableList<Track>{
        val dtoList = repository.load()

        tracks = dtoList.map {
            dto -> Track(dto.trackId, dto.trackName, dto.artistName, dto.trackTimeMillis,
            dto.artworkUrl100, dto.collectionName, dto.releaseDate,
            dto.primaryGenreName,dto.country,dto.previewUrl)
        } as MutableList<Track>

        return tracks
    }

    override fun save() {
        val tracks = tracks.map {
            track -> TrackDto(track.trackId, track.trackName, track.artistName, track.trackTimeMillis,
            track.artworkUrl100, track.collectionName, track.releaseDate.toString(),
            track.primaryGenreName,track.country,track.previewUrl)
        } as MutableList<TrackDto>
        repository.save(tracks)
    }

    override fun clear() {
        tracks.clear()
    }
}
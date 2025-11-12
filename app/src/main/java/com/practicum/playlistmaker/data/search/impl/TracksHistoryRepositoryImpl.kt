package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.search.DataStorage
import com.practicum.playlistmaker.data.search.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.TracksHistoryRepository

class TracksHistoryRepositoryImpl(private val dataStorage: DataStorage): TracksHistoryRepository {
    override fun load(): MutableList<Track> {
        val tracksDto = dataStorage.load()

       val tracks = tracksDto.map {
                dto ->
            Track(
                dto.trackId, dto.trackName, dto.artistName, dto.trackTimeMillis,
                dto.artworkUrl100, dto.collectionName, dto.releaseDate,
                dto.primaryGenreName, dto.country, dto.previewUrl
            )
        } as MutableList<Track>

        return tracks
    }

    override fun save(tracks: MutableList<Track>) {
        val tracks = tracks.map {
            track ->
            TrackDto(
                track.trackId, track.trackName, track.artistName, track.trackTimeMillis,
                track.artworkUrl100, track.collectionName, track.releaseDate.toString(),
                track.primaryGenreName, track.country, track.previewUrl
            )
        } as MutableList<TrackDto>
       dataStorage.save(tracks)
    }
}
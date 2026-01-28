package com.practicum.playlistmaker.data.search.map

import com.practicum.playlistmaker.data.search.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track

class SearchMapper {

    fun map(track: Track): TrackDto{
        return TrackDto(
            track.trackId, track.trackName, track.artistName, track.trackTimeMillis,
            track.artworkUrl100, track.collectionName, track.releaseDate.toString(),
            track.primaryGenreName, track.country, track.previewUrl
        )
    }

    fun map(track: TrackDto): Track{
        return Track(
            track.trackId, track.trackName, track.artistName, track.trackTimeMillis,
            track.artworkUrl100, track.collectionName, track.releaseDate,
            track.primaryGenreName, track.country, track.previewUrl
        )
    }
}
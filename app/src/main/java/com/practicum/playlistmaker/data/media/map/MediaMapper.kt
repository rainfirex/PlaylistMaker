package com.practicum.playlistmaker.data.media.map

import com.practicum.playlistmaker.data.media.dto.TrackMedia
import com.practicum.playlistmaker.domain.models.Track

class MediaMapper {

    fun map(track :Track): TrackMedia{
        return TrackMedia(
            track.trackId,
            track.trackName,
            track.artistName,
            track.collectionName,
            track.releaseDate,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.previewUrl,
            track.primaryGenreName,
            track.country,
        )
    }

    fun map(track: TrackMedia): Track{
        return Track(
            track.id,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}
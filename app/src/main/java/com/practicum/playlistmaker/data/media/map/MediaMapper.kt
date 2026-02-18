package com.practicum.playlistmaker.data.media.map

import com.practicum.playlistmaker.data.media.dto.Entity
import com.practicum.playlistmaker.data.media.dto.TrackMedia
import com.practicum.playlistmaker.data.media.dto.TrackPlaylist
import com.practicum.playlistmaker.domain.models.Playlist
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

    fun map(playlist: Playlist): Entity{
        return Entity(
            playlist.id,
            playlist.namePlaylist,
            playlist.description,
            playlist.pathImage,
            playlist.tracks,
            playlist.count
        )
    }

    fun map(entity: Entity): Playlist{
        return Playlist(
            entity.id,
            entity.namePlaylist,
            entity.description,
            entity.pathImage,
            entity.tracks,
            entity.count
        )
    }

    fun mapTrackPlaylist(track :Track): TrackPlaylist{
        return TrackPlaylist(
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
}
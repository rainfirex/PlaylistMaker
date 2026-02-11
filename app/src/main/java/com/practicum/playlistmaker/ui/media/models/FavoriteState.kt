package com.practicum.playlistmaker.ui.media.models

import com.practicum.playlistmaker.domain.models.Track

sealed interface FavoriteState {
    data class Tracks (val tracks: List<Track>) :FavoriteState
}
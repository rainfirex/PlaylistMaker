package com.practicum.playlistmaker.ui.search.models

import androidx.annotation.DrawableRes
import com.practicum.playlistmaker.domain.models.Track

sealed interface SearchState {
    object Loading : SearchState
    data class Error(val stringId: Int, @DrawableRes val drawableId: Int) : SearchState
    data class SearchResult(val tracks: List<Track>): SearchState
    data class HistoryResult(val tracks: List<Track>): SearchState
}
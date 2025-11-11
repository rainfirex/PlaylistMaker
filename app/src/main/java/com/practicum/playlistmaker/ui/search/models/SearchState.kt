package com.practicum.playlistmaker.ui.search.models

import androidx.annotation.DrawableRes
import com.practicum.playlistmaker.domain.models.Track

interface SearchState {
    object Loading : SearchState
    data class Error(val stringId: Int, @DrawableRes val drawableId: Int) : SearchState
    data class SearchResult(val tracks: MutableList<Track>): SearchState
    data class HistoryResult(val tracks: MutableList<Track>): SearchState
}
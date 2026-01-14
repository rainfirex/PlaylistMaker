package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.TracksHistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksSearchInteractor
import com.practicum.playlistmaker.ui.search.models.SearchState
import com.practicum.playlistmaker.ui.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(private val searchProvider: TracksSearchInteractor, private val historyProvider: TracksHistoryInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private var searchText: String? = null

    private val searchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true){
        searchText -> searchTracks(searchText)
    }

    init {
        val tracks = historyProvider.loadTracks()
        renderState(
            SearchState.HistoryResult(tracks)
        )
    }

    fun searchTracksDebounce(searchText: String){
        if (this.searchText == searchText) {
            return
        }

        this.searchText = searchText
        searchDebounce(searchText)
    }

    private fun searchTracks(searchText: String?){
        if(searchText.isNullOrEmpty()) return

        renderState(SearchState.Loading)

        viewModelScope.launch {
            searchProvider.searchTracks(searchText).collect { result ->
                when{
                    result.isSuccess -> {
                        val tracks = result.getOrNull()
                        if(tracks?.isNotEmpty() == true){
                            renderState(
                                SearchState.SearchResult(tracks)
                            )
                        }
                        else{
                            renderState(
                                SearchState.Error(
                                    R.string.nothing_found,
                                    R.drawable.ic_nothing_found_120
                                )
                            )
                        }
                    }
                    result.isFailure-> {
                        renderState(
                            SearchState.Error(R.string.no_internet, R.drawable.ic_no_internet_120)
                        )
                    }
                }
            }
        }
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun saveHistory() {
        historyProvider.save()
    }

    fun addHistory(track: Track, i: Int) {
        historyProvider.addTrack(track, i)
        renderState(
            SearchState.HistoryResult(historyProvider.getTracks())
        )
    }

    fun clearHistory(){
        historyProvider.clear()
    }

    companion object{
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
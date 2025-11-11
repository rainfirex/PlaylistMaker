package com.practicum.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.TracksHistoryInter
import com.practicum.playlistmaker.domain.search.TracksSearchInter
import com.practicum.playlistmaker.domain.search.consumer.ConsumerData
import com.practicum.playlistmaker.domain.search.consumer.TrackSearchConsumer
import com.practicum.playlistmaker.ui.search.models.SearchState

class SearchViewModel(private val searchProvider: TracksSearchInter, private val historyProvider: TracksHistoryInter) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private val handler = Handler(Looper.getMainLooper())
    private var searchTracksRunnable = Runnable { searchTracks() }

    private var searchText: String? = null

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

        handler.removeCallbacks(searchTracksRunnable)
        handler.postDelayed(searchTracksRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchTracks(){
        if(searchText.isNullOrEmpty()) return

        renderState(SearchState.Loading)

        searchProvider.searchTracks(searchText!!, object : TrackSearchConsumer<MutableList<Track>> {
            override fun consume(data: ConsumerData<MutableList<Track>>) {
                handler.post {
                    when(data){
                        is ConsumerData.Data -> {
                            val tracks = data.value

                            if(tracks.isNotEmpty() == true){
                                renderState(
                                    SearchState.SearchResult(tracks)
                                )
                            }
                            else{
                                renderState(
                                    SearchState.Error(R.string.nothing_found, R.drawable.ic_nothing_found_120)
                                )
                            }
                        }
                        is ConsumerData.Error -> {
                            renderState(
                                SearchState.Error(R.string.no_internet, R.drawable.ic_no_internet_120)
                            )
                        }
                    }
                }
            }
        })
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchTracksRunnable)
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

        fun getFactory() : ViewModelProvider.Factory{
            return viewModelFactory{
                initializer {
                    val searchProvider = Creator.providerTracksSearchInter()
                    val historyProvider = Creator.providerTracksHistoryInter()
//                    val ctx = (this[APPLICATION_KEY] as Application)
                    SearchViewModel(searchProvider, historyProvider)
                }
            }
        }
    }
}
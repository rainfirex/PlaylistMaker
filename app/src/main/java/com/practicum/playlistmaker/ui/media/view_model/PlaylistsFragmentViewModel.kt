package com.practicum.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.PlaylistInteractor
import com.practicum.playlistmaker.ui.common.models.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    fun getPlaylists(){
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlists ->
                renderState(PlaylistsState.Playlists(playlists))
            }
        }
    }

    private fun renderState(state: PlaylistsState){
        stateLiveData.postValue(state)
    }
}
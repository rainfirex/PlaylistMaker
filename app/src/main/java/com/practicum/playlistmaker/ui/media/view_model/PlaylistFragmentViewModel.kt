package com.practicum.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.PlaylistInteractor
import com.practicum.playlistmaker.ui.common.models.PlaylistState
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = stateLiveData

    fun getPlaylists(){
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlists ->
                renderState(PlaylistState.Playlists(playlists))
            }
        }
    }

    private fun renderState(state: PlaylistState){
        stateLiveData.postValue(state)
    }
}
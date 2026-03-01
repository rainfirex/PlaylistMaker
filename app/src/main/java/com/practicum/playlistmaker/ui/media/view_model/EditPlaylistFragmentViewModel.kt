package com.practicum.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.PlaylistInteractor
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.ui.common.models.DataState
import kotlinx.coroutines.launch

class EditPlaylistFragmentViewModel(val playlistInteractor: PlaylistInteractor) : CreatePlaylistFragmentViewModel(playlistInteractor) {

    private val stateLiveData = MutableLiveData<DataState>()
    fun observeStatePlaylist(): LiveData<DataState> = stateLiveData

    var currPlaylist: Playlist? = null

    fun getPlaylist(playlistId: Int){
        viewModelScope.launch {
            playlistInteractor.getPlaylist(playlistId).collect { playlist ->
                currPlaylist = playlist
                stateLiveData.postValue(DataState.DataPlaylist(playlist))
            }
        }
    }

    fun updatePlaylist(namePlaylist: String, description: String, pathImage: String?){
        val playlist = currPlaylist
        if(playlist != null){
            viewModelScope.launch{
                playlistInteractor.updatePlaylist(playlist, namePlaylist, description, pathImage)
            }
        }
    }
}
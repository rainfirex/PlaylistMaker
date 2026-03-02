package com.practicum.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.PlaylistInteractor
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.ui.common.models.DataState
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(private val playlistInteractor: PlaylistInteractor, private val sharingInteractor: SharingInteractor):  ViewModel() {

    private var statePlaylist = MutableLiveData<DataState>()
    fun observeStatePlaylist(): LiveData<DataState> = statePlaylist

    private var stateTracks = MutableLiveData<DataState>()
    fun observeStateTracks(): LiveData<DataState> = stateTracks

    private var stateRemovePlaylist = MutableLiveData<Int>()
    fun observeStateRemovePlaylist(): LiveData<Int> = stateRemovePlaylist

    fun getPlaylist(id: Int){
        viewModelScope.launch{
            playlistInteractor.getPlaylist(id).collect { playlist ->
                statePlaylist.postValue(DataState.DataPlaylist(playlist))
                getTracks(playlist.tracks ?: "")
            }
        }
    }

    private fun getTracks(tracks: String){
        viewModelScope.launch{
            playlistInteractor.getTracks(tracks).collect { tracks ->
                stateTracks.postValue(DataState.DataTracks(tracks))
            }
        }
    }

    fun removeTrackFromPlaylist(playlist: Playlist, trackId: Int){
        viewModelScope.launch{
            playlistInteractor.updateTracks(playlist, trackId).collect { countRow ->
                if(countRow > 0){
                    getPlaylist(playlist.id)
                    removeTrackIsNotUse(trackId)
                }
            }
        }
    }

    private fun removeTrackIsNotUse(trackId: Int){
        viewModelScope.launch{
            playlistInteractor.removeTrack(trackId)
        }
    }

    fun sharePlaylist(playlist: Playlist, tracks: List<Track>){
        sharingInteractor.sharePlaylist(playlist, tracks)
    }

    fun removePlaylist(playlist: Playlist){
        viewModelScope.launch {
            playlistInteractor.removePlaylist(playlist).collect { countRow ->
                if(countRow > 0){
                    stateRemovePlaylist.postValue(countRow)
                }
            }
        }
    }
}
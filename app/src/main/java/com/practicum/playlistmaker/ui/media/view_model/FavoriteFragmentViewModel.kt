package com.practicum.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.MediaInteractor
import com.practicum.playlistmaker.ui.media.models.FavoriteState
import kotlinx.coroutines.launch

class FavoriteFragmentViewModel(private val mediaInteractor: MediaInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteState>()
    fun observeState(): LiveData<FavoriteState> = stateLiveData

    fun getTracks(){
        viewModelScope.launch{
            mediaInteractor.getTracks().collect { tracks ->
                renderState(FavoriteState.Tracks(tracks))
            }
        }
    }

    fun renderState(state: FavoriteState){
        stateLiveData.postValue(state)
    }
}
package com.practicum.playlistmaker.ui.media.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.PlaylistInteractor
import com.practicum.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistFragmentViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    fun createPlaylist(namePlaylist: String, description: String?, pathImage: String?){
        val pl = Playlist(0, namePlaylist, description, pathImage, null, 0)
        viewModelScope.launch{
            playlistInteractor.insertPlaylist(pl)
        }
    }
}
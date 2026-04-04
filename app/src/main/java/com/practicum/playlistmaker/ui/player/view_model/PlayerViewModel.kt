package com.practicum.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.MediaInteractor
import com.practicum.playlistmaker.domain.media.PlaylistInteractor
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.services.PlayMusicServiceControl
import com.practicum.playlistmaker.ui.common.models.PlaylistsState
import com.practicum.playlistmaker.ui.player.enums.StateMediaPlayer
import com.practicum.playlistmaker.ui.player.models.DataStateMediaPlayer
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val mediaInteractor: MediaInteractor,
    private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private var stateMediaPlayer = MutableLiveData<DataStateMediaPlayer>( DataStateMediaPlayer(StateMediaPlayer.STATE_DEFAULT) )
    fun observeStateMediaPlayer(): LiveData<DataStateMediaPlayer> = stateMediaPlayer

    private var stateFavoriteTrack = MutableLiveData<Boolean>(false)
    fun observeStateFavoriteTrack(): LiveData<Boolean> = stateFavoriteTrack

    private var statePlaylists = MutableLiveData<PlaylistsState>()
    fun observeStatePlaylists(): LiveData<PlaylistsState> = statePlaylists

    private var stateAddTrackToPlaylist = MutableLiveData<Pair<Int, String>>()
    fun observeStateAddTrackToPlaylist(): LiveData<Pair<Int, String>> = stateAddTrackToPlaylist

    fun addTrack(playlist: Playlist, track: Track){
        if (playlist.tracks != null){
            val ids = playlist.tracks.trim().split(DELIMITER)
            val hasId = track.trackId.toString() in ids
            if(hasId){
                stateAddTrackToPlaylist.postValue(Pair(STATUS_FAIL_ADD_TRACK, playlist.namePlaylist))
                return
            }
        }

        viewModelScope.launch {
            playlistInteractor.addTrack(track, playlist)
            stateAddTrackToPlaylist.postValue(Pair(STATUS_SUCCESS_ADD_TRACK, playlist.namePlaylist))
        }
    }

    fun getPlaylist(){
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playLists ->
                renderState(PlaylistsState.Playlists(playLists))
            }
        }
    }

    fun renderState(state: PlaylistsState){
        statePlaylists.postValue(state)
    }

    fun playbackControl() {
        when(stateMediaPlayer.value?.state){
            StateMediaPlayer.STATE_PLAYING -> audioPlayer?.pausePlayer()
            StateMediaPlayer.STATE_PREPARED, StateMediaPlayer.STATE_PAUSED -> audioPlayer?.startPlayer()
            StateMediaPlayer.STATE_DEFAULT,
            null -> {}
        }
    }

    override fun onCleared() {
        super.onCleared()
//        mediaPlayer.release()
        removePlayMusicService()
    }

    fun setFavoriteTrack(isFavorite: Boolean){
        stateFavoriteTrack.postValue(isFavorite)
    }

    fun changeFavoriteTrack(track: Track){
        val isFavorite = !track.isFavorite
        when(isFavorite){
            true -> {
                viewModelScope.launch{
                    mediaInteractor.insertTrack(track)
                }
            }
            false -> {
                viewModelScope.launch{
                    mediaInteractor.removeTrack(track)
                }
            }
        }

        setFavoriteTrack(isFavorite)
    }

    // region PlayMusicService

    private var audioPlayer: PlayMusicServiceControl? = null

    fun setPlayMusicService(audioPlayer: PlayMusicServiceControl){
        this.audioPlayer = audioPlayer

        viewModelScope.launch {
            audioPlayer.getPlayerState().collect {
                stateMediaPlayer.postValue(it)
            }
        }
    }

    fun removePlayMusicService(){
        audioPlayer = null
    }

    fun showNotificationService(){
        if(stateMediaPlayer.value?.state == StateMediaPlayer.STATE_PLAYING){
            audioPlayer?.startNotification()
        }
    }

    fun stopNotificationService(){
        audioPlayer?.stopNotification()
    }

    //endregion

    companion object{
        private const val STATUS_FAIL_ADD_TRACK = -1
        private const val STATUS_SUCCESS_ADD_TRACK = 1
        private const val DELIMITER = ";"
    }
}
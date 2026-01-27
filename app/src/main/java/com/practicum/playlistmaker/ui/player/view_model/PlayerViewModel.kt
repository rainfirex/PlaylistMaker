package com.practicum.playlistmaker.ui.player.view_model

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.media.MediaInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.player.enums.StateMediaPlayer
import com.practicum.playlistmaker.ui.player.models.DataStateMediaPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val url: String, private val trackTimeMillis: Int,
                      private var mediaPlayer: MediaPlayer, private val mediaInteractor: MediaInteractor): ViewModel() {

    private var stateMediaPlayer = MutableLiveData<DataStateMediaPlayer>( DataStateMediaPlayer(StateMediaPlayer.STATE_DEFAULT) )
    fun observeStateMediaPlayer(): LiveData<DataStateMediaPlayer> = stateMediaPlayer

    private var timerJob: Job? = null

    init {
        preparePlayer(url)
    }

    private fun getCurrentPosition(timeMillis: Int): String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }

    private fun preparePlayer(url: String) {
        val defaultTime = getCurrentPosition(trackTimeMillis)

        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateMediaPlayer.postValue(DataStateMediaPlayer(StateMediaPlayer.STATE_PREPARED, defaultTime))
        }
        mediaPlayer.setOnCompletionListener {
            timerJob?.cancel()
            stateMediaPlayer.postValue(DataStateMediaPlayer(StateMediaPlayer.STATE_PREPARED, "00:00"))
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(DELAY)
                val currentPosition = getCurrentPosition(mediaPlayer.currentPosition)
                val currentState = stateMediaPlayer.value!!.state
                stateMediaPlayer.postValue(DataStateMediaPlayer(currentState, currentPosition))
            }
        }
    }

    fun playbackControl() {
        when(stateMediaPlayer.value?.state){
            StateMediaPlayer.STATE_PLAYING -> pausePlayer()
            StateMediaPlayer.STATE_PREPARED, StateMediaPlayer.STATE_PAUSED -> startPlayer()
            StateMediaPlayer.STATE_DEFAULT, null -> {}
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        stateMediaPlayer.postValue(DataStateMediaPlayer(StateMediaPlayer.STATE_PLAYING, getCurrentPosition(mediaPlayer.currentPosition)))

        startTimer()
    }

    fun pausePlayer() {
        mediaPlayer.pause()

        timerJob?.cancel()

        val currentTime = stateMediaPlayer.value?.timerTrack ?: ""
        stateMediaPlayer.postValue(
            DataStateMediaPlayer(
                StateMediaPlayer.STATE_PAUSED,
                currentTime
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }

    fun addFavoriteTrack(track: Track){
        viewModelScope.launch{
            mediaInteractor.insertTrack(track)
        }
    }

    fun unFavoriteTrack(track: Track){
        viewModelScope.launch {
            mediaInteractor.removeTrack(track)
        }
    }

    companion object{
        private const val DELAY = 300L
    }
}
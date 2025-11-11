package com.practicum.playlistmaker.ui.player.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.ui.player.enums.StateMediaPlayer
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val url: String, private val trackTimeMillis: Int): ViewModel() {

    private var playerState = MutableLiveData<StateMediaPlayer>(StateMediaPlayer.STATE_DEFAULT)
    fun observePlayerState(): LiveData<StateMediaPlayer> = playerState

    private var timerTrack = MutableLiveData<String>()
    fun observeTimerTrack(): LiveData<String> = timerTrack

    private var mediaPlayer = MediaPlayer()

    private val handler = Handler(Looper.getMainLooper())
    private val timerTaskRunnable = timerTask()


    init {
        preparePlayer(url)
    }

    private fun preparePlayer(url: String) {
        val defaultTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.postValue(StateMediaPlayer.STATE_PREPARED)
            timerTrack.postValue(defaultTime)
        }
        mediaPlayer.setOnCompletionListener {
            playerState.postValue(StateMediaPlayer.STATE_PREPARED)

            handler.removeCallbacks(timerTaskRunnable)
            timerTrack.postValue(defaultTime)
        }
    }

    private fun timerTask(): Runnable {
        return object: Runnable{
            override fun run(){
                timerTrack.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition))
                handler.postDelayed(this, DELAY)
            }
        }
    }

    fun playbackControl() {
        when(playerState.value) {
            StateMediaPlayer.STATE_PLAYING -> pausePlayer()
            StateMediaPlayer.STATE_PREPARED, StateMediaPlayer.STATE_PAUSED -> startPlayer()
            StateMediaPlayer.STATE_DEFAULT, null -> {}
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState.postValue(StateMediaPlayer.STATE_PLAYING)
        handler.post(timerTaskRunnable)
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState.postValue(StateMediaPlayer.STATE_PAUSED)
        handler.removeCallbacks(timerTaskRunnable)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        handler.removeCallbacks(timerTaskRunnable)
    }

    companion object{
        private const val DELAY = 500L

        fun getFactory(url: String, trackTimeMillis: Int) : ViewModelProvider.Factory{
            return viewModelFactory{
                initializer {
                    PlayerViewModel(url, trackTimeMillis)
                }
            }
        }
    }
}
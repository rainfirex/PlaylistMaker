package com.practicum.playlistmaker.ui.player.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.ui.player.enums.StateMediaPlayer
import com.practicum.playlistmaker.ui.player.models.DataStateMediaPlayer
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val url: String, private val trackTimeMillis: Int, private var mediaPlayer: MediaPlayer): ViewModel() {

    private var stateMediaPlayer = MutableLiveData<DataStateMediaPlayer>( DataStateMediaPlayer(StateMediaPlayer.STATE_DEFAULT) )
    fun observeStateMediaPlayer(): LiveData<DataStateMediaPlayer> = stateMediaPlayer

    private val handler = Handler(Looper.getMainLooper())
    private val timerTaskRunnable = timerTask()

    private var timerJob: Job? = null

    init {
        preparePlayer(url)
    }

    private fun preparePlayer(url: String) {
        val defaultTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateMediaPlayer.postValue(DataStateMediaPlayer(StateMediaPlayer.STATE_PREPARED, defaultTime))
        }
        mediaPlayer.setOnCompletionListener {
            handler.removeCallbacks(timerTaskRunnable)
            stateMediaPlayer.postValue(DataStateMediaPlayer(StateMediaPlayer.STATE_PREPARED, defaultTime))
        }
    }

    private fun timerTask(): Runnable {
        return object: Runnable{
            override fun run(){
                val currentPosition = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                val currentState = stateMediaPlayer.value?.state
                if(currentState != null){
                    stateMediaPlayer.postValue(
                        DataStateMediaPlayer(currentState, currentPosition)
                    )
                }

                handler.postDelayed(this, DELAY)
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
        stateMediaPlayer.postValue(DataStateMediaPlayer(StateMediaPlayer.STATE_PLAYING))

        handler.post(timerTaskRunnable)
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

        handler.removeCallbacks(timerTaskRunnable)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        handler.removeCallbacks(timerTaskRunnable)
    }

    companion object{
        private const val DELAY = 500L
    }
}
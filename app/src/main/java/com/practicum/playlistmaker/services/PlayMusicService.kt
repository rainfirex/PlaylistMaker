package com.practicum.playlistmaker.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.player.enums.StateMediaPlayer
import com.practicum.playlistmaker.ui.player.models.DataStateMediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

internal class PlayMusicService: Service(), PlayMusicServiceControl {

    private val binder = PlayMusicServiceBinder()

    private var songUrl = ""
    private var songTitle = ""
    private var songArtist = ""
    private var trackTimeMillis: Int = 0

    private var mediaPlayer: MediaPlayer? = null
    private var timerJob: Job? = null


    private val playerState = MutableStateFlow<DataStateMediaPlayer>(DataStateMediaPlayer(StateMediaPlayer.STATE_DEFAULT))
    val playerStateFlow = playerState.asStateFlow()

    override fun getPlayerState(): StateFlow<DataStateMediaPlayer> {
        return  playerStateFlow
    }

    inner class PlayMusicServiceBinder : Binder(){
        fun getService(): PlayMusicService = this@PlayMusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        songUrl = intent?.getStringExtra(SONG_URL) ?: ""
        songTitle = intent?.getStringExtra(SONG_TITLE) ?: ""
        songArtist = intent?.getStringExtra(SONG_ARTIST) ?: ""

        trackTimeMillis = intent?.getIntExtra(SONG_TIME, 0) ?: 0

        initMediaPlayer()

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        mediaPlayer = MediaPlayer()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    //region Player

    override fun startPlayer() {
        mediaPlayer?.start()
        playerState.value = DataStateMediaPlayer(StateMediaPlayer.STATE_PLAYING, getCurrentPosition(mediaPlayer?.currentPosition ?: 0))
        startTimer()
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()

        timerJob?.cancel()

        val currentTime = playerState.value.timerTrack
        playerState.value = DataStateMediaPlayer(StateMediaPlayer.STATE_PAUSED, currentTime)
    }

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(DELAY)
                val currentPosition = getCurrentPosition(mediaPlayer?.currentPosition ?: 0)
                val currentState = playerState.value.state
                playerState.value = DataStateMediaPlayer(currentState, currentPosition)
            }
        }
    }

    private fun getCurrentPosition(timeMillis: Int): String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }

    private fun initMediaPlayer() {
        if (songUrl.isEmpty()) return

//        val defaultTime = getCurrentPosition(trackTimeMillis)

        mediaPlayer?.setDataSource(songUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            playerState.value = DataStateMediaPlayer(StateMediaPlayer.STATE_PREPARED, DEFAULT_SONG_TIME)
        }
        mediaPlayer?.setOnCompletionListener {
            playerState.value = DataStateMediaPlayer(StateMediaPlayer.STATE_PREPARED, DEFAULT_SONG_TIME)
            stopNotification()
        }
    }

    private fun releasePlayer() {
        mediaPlayer?.stop()
        timerJob?.cancel()
        playerState.value = DataStateMediaPlayer(StateMediaPlayer.STATE_DEFAULT)
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    //endregion

    //region Notification

    override fun startNotification(){
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    override fun stopNotification(){
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun createServiceNotification(): Notification {
        val text = "$songArtist - $songTitle"

        val appName = getString(R.string.app_name)

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(appName)
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = NOTIFICATION_CHANNEL_DESCRIPTION

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else 0
    }

    //endregion

    private companion object{
        private const val DELAY = 300L
        const val DEFAULT_SONG_TIME = "00:00"
        const val SONG_URL = "song_url"
        const val SONG_TITLE = "song_title"
        const val SONG_ARTIST = "song_artist"
        const val SONG_TIME = "song_time"
        const val SERVICE_NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val NOTIFICATION_CHANNEL_NAME = "Play music service"
        const val NOTIFICATION_CHANNEL_DESCRIPTION = "Service for playing music"
    }
}
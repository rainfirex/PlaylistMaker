package com.practicum.playlistmaker.services

import android.R
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
import kotlinx.coroutines.Job

internal class PlayMusicService: Service() {

    private val binder = PlayMusicServiceBinder()
    private var songUrl = ""
    private var mediaPlayer: MediaPlayer? = null
    private var timerJob: Job? = null

    inner class PlayMusicServiceBinder : Binder(){
        fun getService(): PlayMusicService = this@PlayMusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        songUrl = intent?.getStringExtra(SONG_URL) ?: ""
        initMediaPlayer()

        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            getForegroundServiceTypeConstant()
        )
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

    private fun initMediaPlayer() {
        if (songUrl.isEmpty()) return

        mediaPlayer?.setDataSource(songUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
//            _playerState.value = PlayerState.Prepared()
        }
        mediaPlayer?.setOnCompletionListener {
//            _playerState.value = PlayerState.Prepared()
        }
    }

    private fun releasePlayer() {
        mediaPlayer?.stop()
        timerJob?.cancel()
//        _playerState.value = PlayerState.Default()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    //endregion

    //region Notification

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Music foreground service")
            .setContentText("Our service is working right now!")
            .setSmallIcon(R.drawable.ic_notification_overlay)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
           "Play music service",
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
        const val SONG_URL = "song_url"
        const val SERVICE_NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val NOTIFICATION_CHANNEL_DESCRIPTION = "Service for playing music"
    }
}
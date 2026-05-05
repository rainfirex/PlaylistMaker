package com.practicum.playlistmaker.services

import com.practicum.playlistmaker.ui.player.models.DataStateMediaPlayer
import kotlinx.coroutines.flow.StateFlow

interface PlayMusicServiceControl {

    fun getPlayerState(): StateFlow<DataStateMediaPlayer>
    fun startPlayer()
    fun pausePlayer()
    fun startNotification()
    fun stopNotification()
}
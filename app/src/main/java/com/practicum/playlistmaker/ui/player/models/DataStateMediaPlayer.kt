package com.practicum.playlistmaker.ui.player.models

import com.practicum.playlistmaker.ui.player.enums.StateMediaPlayer

data class DataStateMediaPlayer (val state: StateMediaPlayer, val timerTrack: String = "")
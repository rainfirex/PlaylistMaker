package com.practicum.playlistmaker.ui.main.view_model

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.config.ConfigInteractor

class MainViewModel(private val configInteractor: ConfigInteractor): ViewModel() {

    fun loadTheme(){
        configInteractor.getTheme()
    }
}
package com.practicum.playlistmaker.ui.root.view_model

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.config.ConfigInteractor

class RootViewModel(private val configInteractor: ConfigInteractor): ViewModel() {

    fun loadTheme(){
        configInteractor.getTheme()
    }
}
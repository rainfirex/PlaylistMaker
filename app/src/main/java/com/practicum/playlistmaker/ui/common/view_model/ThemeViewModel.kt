package com.practicum.playlistmaker.ui.common.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.config.ConfigInteractor

class ThemeViewModel(private val configInteractor: ConfigInteractor): ViewModel() {
    private val isDarkTheme = MutableLiveData<Boolean>()
    fun observeIsDarkTheme(): LiveData<Boolean> = isDarkTheme

    fun loadTheme(){
        isDarkTheme.postValue(configInteractor.getTheme())
    }
}
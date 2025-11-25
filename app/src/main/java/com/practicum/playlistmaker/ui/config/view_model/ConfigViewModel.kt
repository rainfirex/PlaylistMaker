package com.practicum.playlistmaker.ui.config.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.config.ConfigInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor

class ConfigViewModel(private val sharingInteractor: SharingInteractor, private val configInteractor: ConfigInteractor): ViewModel() {

    private val isDarkTheme = MutableLiveData<Boolean>()
    fun observeIsDarkTheme(): LiveData<Boolean> = isDarkTheme

    fun setTheme(isChecked: Boolean){
        configInteractor.setTheme(isChecked)
    }

    fun loadTheme(){
        isDarkTheme.postValue(configInteractor.getTheme())
    }

    fun openSupport(subject: String, message: String){
        sharingInteractor.openSupport(subject, message)
    }

    fun shareApp(url: String){
        sharingInteractor.shareApp(url)
    }

    fun openTerms(url: String){
        sharingInteractor.openTerms(url)
    }
}
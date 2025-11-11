package com.practicum.playlistmaker.ui.config.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.config.ConfigInter
import com.practicum.playlistmaker.domain.sharing.SharingInter

class ConfigViewModel(private val sharingInter: SharingInter, private val configInter: ConfigInter, private val ctx: Context): ViewModel() {

    private val isDarkTheme = MutableLiveData<Boolean>()
    fun observeIsDarkTheme(): LiveData<Boolean> = isDarkTheme

    fun setTheme(isChecked: Boolean){
        (ctx as App).switchTheme(isChecked)
        configInter.setTheme(isChecked)
    }

    fun loadTheme(){
        isDarkTheme.postValue(configInter.getTheme())
    }

    fun openSupport(subject: String, message: String){
        sharingInter.openSupport(subject, message)
    }

    fun shareApp(url: String){
        sharingInter.shareApp(url)
    }

    fun openTerms(url: String){
        sharingInter.openTerms(url)
    }

    companion object{
        fun getFactory() : ViewModelProvider.Factory {
            return viewModelFactory {
                initializer{
                    val configProvider = Creator.providerConfig()
                    val sharingProvider = Creator.providerSharingInter()
                    val app = (this[APPLICATION_KEY] as Application)
                    ConfigViewModel(sharingProvider, configProvider, app)
                }
            }
        }
    }
}
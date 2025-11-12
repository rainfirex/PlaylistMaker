package com.practicum.playlistmaker.ui.config.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
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

    companion object{
        fun getFactory() : ViewModelProvider.Factory {
            return viewModelFactory {
                initializer{
                    val configProvider = Creator.providerConfig()
                    val sharingProvider = Creator.providerSharingInter()
                    ConfigViewModel(sharingProvider, configProvider)
                }
            }
        }
    }
}
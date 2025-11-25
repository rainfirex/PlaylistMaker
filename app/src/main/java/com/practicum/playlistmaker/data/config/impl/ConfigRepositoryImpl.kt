package com.practicum.playlistmaker.data.config.impl

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.data.config.ConfigStorage
import com.practicum.playlistmaker.domain.config.ConfigRepository

class ConfigRepositoryImpl(private val configStorage: ConfigStorage): ConfigRepository {

    override fun getTheme(): Boolean {
        val isChecked = configStorage.getTheme()
        switch(isChecked)
        return isChecked
    }

    override fun setTheme(isChecked: Boolean) {
        configStorage.setTheme(isChecked)
        switch(isChecked)
    }

    private fun switch(darkThemeEnabled: Boolean){
        AppCompatDelegate.setDefaultNightMode(
            if(darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            }
            else{
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
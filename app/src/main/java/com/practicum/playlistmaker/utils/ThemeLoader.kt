package com.practicum.playlistmaker.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.data.config.impl.ConfigRepositoryImpl
import com.practicum.playlistmaker.data.config.db.ConfigDataStorage
import com.practicum.playlistmaker.domain.config.impl.ConfigInterImpl

object ThemeLoader {
    fun load(ctx : Context){
        val r = ConfigInterImpl(ConfigRepositoryImpl(ConfigDataStorage(ctx))).getTheme()
        switch(r)
    }

    fun switch(darkThemeEnabled: Boolean){
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
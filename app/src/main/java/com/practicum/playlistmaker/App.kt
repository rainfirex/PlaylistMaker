package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val CONFIG = "config"
const val THEME = "theme"

class App : Application() {

    //var darkTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(CONFIG, MODE_PRIVATE)
        val darkTheme = sharedPrefs.getBoolean(THEME, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean){
        //darkTheme = darkThemeEnabled
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
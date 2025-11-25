package com.practicum.playlistmaker.data.config.db

import android.content.SharedPreferences
import androidx.core.content.edit
import com.practicum.playlistmaker.data.config.ConfigStorage

class ConfigDataStorage(private val sharedPrefs: SharedPreferences) : ConfigStorage {

    override fun getTheme(): Boolean{
        return sharedPrefs.getBoolean(THEME, false)
    }

    override fun setTheme(value: Boolean) {
        sharedPrefs.edit() {
            putBoolean(THEME, value)
        }
    }

    companion object{
        private const val THEME = "theme"
    }
}
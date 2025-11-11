package com.practicum.playlistmaker.data.db

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.practicum.playlistmaker.data.ConfigStorage
import androidx.core.content.edit

class ConfigDataStorage(ctx: Context) : ConfigStorage{

    private val sharedPrefs = ctx.getSharedPreferences(CONFIG, MODE_PRIVATE)

    override fun getTheme(): Boolean{
        return sharedPrefs.getBoolean(THEME, false)
    }

    override fun setTheme(value: Boolean) {
        sharedPrefs.edit() {
            putBoolean(THEME, value)
        }
    }

    companion object{
        private const val CONFIG = "config"
        private const val THEME = "theme"
    }
}
package com.practicum.playlistmaker.data.config.db

import android.content.Context
import androidx.core.content.edit
import com.practicum.playlistmaker.data.config.ConfigStorage

class ConfigDataStorage(ctx: Context) : ConfigStorage {

    private val sharedPrefs = ctx.getSharedPreferences(CONFIG, Context.MODE_PRIVATE)

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
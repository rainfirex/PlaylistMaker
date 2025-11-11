package com.practicum.playlistmaker.data.config.impl

import com.practicum.playlistmaker.data.config.ConfigStorage
import com.practicum.playlistmaker.domain.config.ConfigRepository

class ConfigRepositoryImpl(private val configStorage: ConfigStorage): ConfigRepository {
    override fun getTheme(): Boolean {
        return configStorage.getTheme()
    }

    override fun setTheme(value: Boolean) {
        configStorage.setTheme(value)
    }
}
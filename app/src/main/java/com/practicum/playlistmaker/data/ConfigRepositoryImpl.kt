package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.domain.api.ConfigRepository

class ConfigRepositoryImpl(private val configStorage: ConfigStorage): ConfigRepository {
    override fun getTheme(): Boolean {
        return configStorage.getTheme()
    }

    override fun setTheme(value: Boolean) {
        configStorage.setTheme(value)
    }
}
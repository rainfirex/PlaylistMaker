package com.practicum.playlistmaker.domain.config.impl

import com.practicum.playlistmaker.domain.config.ConfigInteractor
import com.practicum.playlistmaker.domain.config.ConfigRepository

class ConfigInterImpl(private val configRepository: ConfigRepository): ConfigInteractor {

    override fun getTheme(): Boolean {
        return configRepository.getTheme()
    }

    override fun setTheme(value: Boolean) {
        configRepository.setTheme(value)
    }

}
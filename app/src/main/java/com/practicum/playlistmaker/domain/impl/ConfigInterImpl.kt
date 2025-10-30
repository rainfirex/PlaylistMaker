package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.ConfigInter
import com.practicum.playlistmaker.domain.api.ConfigRepository

class ConfigInterImpl(private val configRepository: ConfigRepository): ConfigInter {

    override fun getTheme(): Boolean {
        return configRepository.getTheme()
    }

    override fun setTheme(value: Boolean) {
        configRepository.setTheme(value)
    }

}
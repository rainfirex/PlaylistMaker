package com.practicum.playlistmaker.domain.config

interface ConfigInteractor {
    fun getTheme(): Boolean
    fun setTheme(value: Boolean)
}
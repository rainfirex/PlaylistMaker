package com.practicum.playlistmaker.domain.config

interface ConfigRepository {
    fun getTheme(): Boolean
    fun setTheme(value: Boolean)
}
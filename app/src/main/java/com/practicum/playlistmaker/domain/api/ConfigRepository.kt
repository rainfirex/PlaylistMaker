package com.practicum.playlistmaker.domain.api

interface ConfigRepository {
    fun getTheme(): Boolean
    fun setTheme(value: Boolean)
}
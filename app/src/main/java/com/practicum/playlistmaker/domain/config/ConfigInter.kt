package com.practicum.playlistmaker.domain.config

interface ConfigInter {
    fun getTheme(): Boolean
    fun setTheme(value: Boolean)
}
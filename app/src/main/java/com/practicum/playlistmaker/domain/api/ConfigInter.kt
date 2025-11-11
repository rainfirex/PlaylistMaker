package com.practicum.playlistmaker.domain.api

interface ConfigInter {
    fun getTheme(): Boolean
    fun setTheme(value: Boolean)
}
package com.practicum.playlistmaker.data.config

interface ConfigStorage {
    fun getTheme(): Boolean
    fun setTheme(value: Boolean)
}
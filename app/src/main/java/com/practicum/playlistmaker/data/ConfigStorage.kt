package com.practicum.playlistmaker.data

interface ConfigStorage {
    fun getTheme(): Boolean
    fun setTheme(value: Boolean)
}
package com.practicum.playlistmaker.domain.sharing

interface SharingInter {
    fun shareApp(url: String)
    fun openTerms(url: String)
    fun openSupport(subject: String,message: String)
}
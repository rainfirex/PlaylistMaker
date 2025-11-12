package com.practicum.playlistmaker.domain.sharing

interface SharingInteractor {
    fun shareApp(url: String)
    fun openTerms(url: String)
    fun openSupport(subject: String,message: String)
}
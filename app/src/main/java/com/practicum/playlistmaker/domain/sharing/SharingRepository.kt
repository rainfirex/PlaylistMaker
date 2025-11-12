package com.practicum.playlistmaker.domain.sharing

interface SharingRepository {
    fun shareLink(string: String)
    fun openLink(string: String)
    fun openEmail(subject: String, message: String)
}
package com.practicum.playlistmaker.data.sharing.impl

import com.practicum.playlistmaker.data.sharing.SharingNavigator
import com.practicum.playlistmaker.data.sharing.dto.EmailDataDto
import com.practicum.playlistmaker.domain.sharing.SharingRepository

class SharingRepositoryImpl(private val navigator: SharingNavigator): SharingRepository {
    override fun shareLink(string: String) {
        navigator.shareLink(string)
    }

    override fun openLink(string: String) {
        navigator.openLink(string)
    }

    override fun openEmail(subject: String, message: String) {
        navigator.openEmail(EmailDataDto(subject, message))
    }

    override fun sharePlaylist(text: String) {
        navigator.sharePlaylist(text)
    }
}
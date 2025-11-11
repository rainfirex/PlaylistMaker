package com.practicum.playlistmaker.domain.sharing.impl

import com.practicum.playlistmaker.data.sharing.dto.EmailDataDto
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import com.practicum.playlistmaker.domain.sharing.SharingInter

class SharingInterImpl(private val externalNavigator: SharingRepository): SharingInter {

    override fun shareApp(url: String) {
        externalNavigator.shareLink(url)
    }

    override fun openTerms(url: String) {
        externalNavigator.openLink(url)
    }

    override fun openSupport(subject: String, message: String) {
        externalNavigator.openEmail(EmailDataDto(subject, message))
    }
}
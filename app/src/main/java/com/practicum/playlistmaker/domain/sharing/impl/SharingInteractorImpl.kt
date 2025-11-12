package com.practicum.playlistmaker.domain.sharing.impl

import com.practicum.playlistmaker.domain.sharing.SharingRepository
import com.practicum.playlistmaker.domain.sharing.SharingInteractor

class SharingInteractorImpl(private val externalNavigator: SharingRepository): SharingInteractor {

    override fun shareApp(url: String) {
        externalNavigator.shareLink(url)
    }

    override fun openTerms(url: String) {
        externalNavigator.openLink(url)
    }

    override fun openSupport(subject: String, message: String) {
        externalNavigator.openEmail(subject, message)
    }
}
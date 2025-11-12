package com.practicum.playlistmaker.data.sharing

import com.practicum.playlistmaker.data.sharing.dto.EmailDataDto

interface SharingNavigator {
    fun shareLink(url: String)
    fun openLink(url: String)
    fun openEmail(dto: EmailDataDto)
}
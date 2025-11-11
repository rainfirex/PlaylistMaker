package com.practicum.playlistmaker.domain.sharing

import com.practicum.playlistmaker.data.sharing.dto.EmailDataDto

interface SharingRepository {
    fun shareLink(string: String)
    fun openLink(string: String)
    fun openEmail(value: EmailDataDto)
}
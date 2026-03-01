package com.practicum.playlistmaker.domain.sharing

import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track

interface SharingInteractor {
    fun shareApp(url: String)
    fun openTerms(url: String)
    fun openSupport(subject: String,message: String)
    fun sharePlaylist(playlist: Playlist, tracks: List<Track>)
}
package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.config.ConfigInteractor
import com.practicum.playlistmaker.domain.config.impl.ConfigInterImpl
import com.practicum.playlistmaker.domain.media.MediaInteractor
import com.practicum.playlistmaker.domain.media.PlaylistInteractor
import com.practicum.playlistmaker.domain.media.impl.MediaInteractorImpl
import com.practicum.playlistmaker.domain.media.impl.PlaylistInteractorImpl
import com.practicum.playlistmaker.domain.search.TracksHistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksSearchInteractor
import com.practicum.playlistmaker.domain.search.impl.TracksHistoryInteractorImpl
import com.practicum.playlistmaker.domain.search.impl.TracksSearchInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val domainModule = module{
    single<ConfigInteractor> {
        ConfigInterImpl(get())
    }

    single<TracksHistoryInteractor> {
        TracksHistoryInteractorImpl(get())
    }

    single<TracksSearchInteractor> {
        TracksSearchInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    single<MediaInteractor> {
        MediaInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}
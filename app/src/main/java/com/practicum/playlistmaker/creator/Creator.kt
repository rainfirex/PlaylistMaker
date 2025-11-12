package com.practicum.playlistmaker.creator

import android.app.Application
import com.practicum.playlistmaker.data.config.impl.ConfigRepositoryImpl
import com.practicum.playlistmaker.data.search.impl.TrackSearchRepositoryImpl
import com.practicum.playlistmaker.data.search.impl.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.data.config.db.ConfigDataStorage
import com.practicum.playlistmaker.data.search.db.TracksHistoryDataStorage
import com.practicum.playlistmaker.data.search.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.data.sharing.navigator.ExternalSharingNavigator
import com.practicum.playlistmaker.domain.config.ConfigInteractor
import com.practicum.playlistmaker.domain.config.ConfigRepository
import com.practicum.playlistmaker.domain.search.TracksHistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksHistoryRepository
import com.practicum.playlistmaker.domain.search.TracksSearchInteractor
import com.practicum.playlistmaker.domain.search.TracksSearchRepository
import com.practicum.playlistmaker.domain.config.impl.ConfigInterImpl
import com.practicum.playlistmaker.domain.search.impl.TracksHistoryInteractorImpl
import com.practicum.playlistmaker.domain.search.impl.TracksSearchInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {

    private lateinit var application: Application

    fun initApp(application: Application){
        this.application = application
    }

    private fun tracksSearchRepository(): TracksSearchRepository {
        return TrackSearchRepositoryImpl(RetrofitNetworkClient())
    }

    fun providerTracksSearchInter(): TracksSearchInteractor {
        return TracksSearchInteractorImpl(tracksSearchRepository())
    }

    private fun tracksHistoryRepository(): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl(TracksHistoryDataStorage(application.applicationContext))
    }

    fun providerTracksHistoryInter(): TracksHistoryInteractor {
        return TracksHistoryInteractorImpl(tracksHistoryRepository())
    }

    private fun configRepository(): ConfigRepository {
        return ConfigRepositoryImpl(ConfigDataStorage(application.applicationContext))
    }

    fun providerConfig(): ConfigInteractor {
        return ConfigInterImpl(configRepository())
    }

    private fun sharingRepository(): SharingRepository{
        return SharingRepositoryImpl(ExternalSharingNavigator(application.applicationContext))
    }

    fun providerSharingInter(): SharingInteractor{
        return SharingInteractorImpl(sharingRepository())
    }
}
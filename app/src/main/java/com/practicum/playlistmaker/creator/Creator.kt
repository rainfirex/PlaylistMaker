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
import com.practicum.playlistmaker.domain.config.ConfigInter
import com.practicum.playlistmaker.domain.config.ConfigRepository
import com.practicum.playlistmaker.domain.search.TracksHistoryInter
import com.practicum.playlistmaker.domain.search.TracksHistoryRepository
import com.practicum.playlistmaker.domain.search.TracksSearchInter
import com.practicum.playlistmaker.domain.search.TracksSearchRepository
import com.practicum.playlistmaker.domain.config.impl.ConfigInterImpl
import com.practicum.playlistmaker.domain.search.impl.TracksHistoryInterImpl
import com.practicum.playlistmaker.domain.search.impl.TracksSearchInterImpl
import com.practicum.playlistmaker.domain.sharing.SharingInter
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import com.practicum.playlistmaker.domain.sharing.impl.SharingInterImpl

object Creator {

    private lateinit var application: Application

    fun initApp(application: Application){
        this.application = application
    }

    private fun tracksSearchRepository(): TracksSearchRepository {
        return TrackSearchRepositoryImpl(RetrofitNetworkClient())
    }

    fun providerTracksSearchInter(): TracksSearchInter {
        return TracksSearchInterImpl(tracksSearchRepository())
    }

    private fun tracksHistoryRepository(): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl(TracksHistoryDataStorage(application.applicationContext))
    }

    fun providerTracksHistoryInter(): TracksHistoryInter {
        return TracksHistoryInterImpl(tracksHistoryRepository())
    }

    private fun configRepository(): ConfigRepository {
        return ConfigRepositoryImpl(ConfigDataStorage(application.applicationContext))
    }

    fun providerConfig(): ConfigInter {
        return ConfigInterImpl(configRepository())
    }

    private fun sharingRepository(): SharingRepository{
        return SharingRepositoryImpl(ExternalSharingNavigator(application.applicationContext))
    }

    fun providerSharingInter(): SharingInter{
        return SharingInterImpl(sharingRepository())
    }
}
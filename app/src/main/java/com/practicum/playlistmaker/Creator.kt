package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.data.ConfigRepositoryImpl
import com.practicum.playlistmaker.data.TrackSearchRepositoryImpl
import com.practicum.playlistmaker.data.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.data.db.ConfigDataStorage
import com.practicum.playlistmaker.data.db.TracksHistoryDataStorage
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.ConfigInter
import com.practicum.playlistmaker.domain.api.ConfigRepository
import com.practicum.playlistmaker.domain.api.TracksHistoryInter
import com.practicum.playlistmaker.domain.api.TracksHistoryRepository
import com.practicum.playlistmaker.domain.api.TracksSearchInter
import com.practicum.playlistmaker.domain.api.TracksSearchRepository
import com.practicum.playlistmaker.domain.impl.ConfigInterImpl
import com.practicum.playlistmaker.domain.impl.TracksHistoryInterImpl
import com.practicum.playlistmaker.domain.impl.TracksSearchInterImpl

object Creator {

    private lateinit var application: Application

    fun initApp(application: Application){
        this.application = application
    }

    private fun tracksSearchRepository(): TracksSearchRepository{
        return TrackSearchRepositoryImpl(RetrofitNetworkClient())
    }

    fun providerTracksSearchInter(): TracksSearchInter{
        return TracksSearchInterImpl(tracksSearchRepository())
    }

    private fun tracksHistoryRepository(): TracksHistoryRepository{
        return TracksHistoryRepositoryImpl(TracksHistoryDataStorage(application.applicationContext))
    }

    fun providerTracksHistoryInter(): TracksHistoryInter{
        return TracksHistoryInterImpl(tracksHistoryRepository())
    }

    private fun configRepository(): ConfigRepository{
        return ConfigRepositoryImpl(ConfigDataStorage(application.applicationContext))
    }

    fun providerConfig(): ConfigInter{
        return ConfigInterImpl(configRepository())
    }
}
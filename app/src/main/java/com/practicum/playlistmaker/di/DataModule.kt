package com.practicum.playlistmaker.di


import com.practicum.playlistmaker.data.config.ConfigStorage
import com.practicum.playlistmaker.data.config.db.ConfigDataStorage
import com.practicum.playlistmaker.data.config.impl.ConfigRepositoryImpl
import com.practicum.playlistmaker.data.search.DataStorage
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.db.TracksHistoryDataStorage
import com.practicum.playlistmaker.data.search.impl.TrackSearchRepositoryImpl
import com.practicum.playlistmaker.data.search.impl.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.data.search.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.sharing.SharingNavigator
import com.practicum.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.data.sharing.navigator.ExternalSharingNavigator
import com.practicum.playlistmaker.domain.config.ConfigRepository
import com.practicum.playlistmaker.domain.search.TracksHistoryRepository
import com.practicum.playlistmaker.domain.search.TracksSearchRepository
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module{

    single<ConfigStorage> {
        ConfigDataStorage(androidContext())
    }

    single<DataStorage> {
        TracksHistoryDataStorage(androidContext())
    }

    single<SharingNavigator> {
        ExternalSharingNavigator(androidContext())
    }

    single<NetworkClient> {
        RetrofitNetworkClient()
    }

    single<ConfigRepository> {
        ConfigRepositoryImpl(get())
    }

    single<TracksSearchRepository> {
        TrackSearchRepositoryImpl(get())
    }

    single<TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(get())
    }

}
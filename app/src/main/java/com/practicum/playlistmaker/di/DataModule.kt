package com.practicum.playlistmaker.di


import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.data.config.ConfigStorage
import com.practicum.playlistmaker.data.config.db.ConfigDataStorage
import com.practicum.playlistmaker.data.config.impl.ConfigRepositoryImpl
import com.practicum.playlistmaker.data.search.DataStorage
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.db.TracksHistoryDataStorage
import com.practicum.playlistmaker.data.search.impl.TrackSearchRepositoryImpl
import com.practicum.playlistmaker.data.search.impl.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.data.search.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.search.network.TrackSearchApi
import com.practicum.playlistmaker.data.sharing.SharingNavigator
import com.practicum.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.data.sharing.navigator.ExternalSharingNavigator
import com.practicum.playlistmaker.domain.config.ConfigRepository
import com.practicum.playlistmaker.domain.search.TracksHistoryRepository
import com.practicum.playlistmaker.domain.search.TracksSearchRepository
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val HISTORY = "history_data"
private const val CONFIG = "config"

val dataModule = module{

    single<TrackSearchApi>{
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackSearchApi::class.java)
    }

    factory { Gson() }

    single(named(HISTORY)) {
        androidContext()
            .getSharedPreferences(HISTORY, Context.MODE_PRIVATE)
    }

    single(named(CONFIG)){
        androidContext()
            .getSharedPreferences(CONFIG, Context.MODE_PRIVATE)
    }

    single<ConfigStorage> {
        ConfigDataStorage(get(named(CONFIG)))
    }

    single<DataStorage> {
        TracksHistoryDataStorage(get(named(HISTORY)), get())
    }

    single<SharingNavigator> {
        ExternalSharingNavigator(androidContext())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
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
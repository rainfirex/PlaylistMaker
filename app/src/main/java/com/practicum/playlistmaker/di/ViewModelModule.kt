package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.config.view_model.ConfigViewModel
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel{
        ConfigViewModel(get(), get())

    }
    viewModel{(url: String, trackTimeMillis: Int) ->
        PlayerViewModel(url, trackTimeMillis)
    }

    viewModel{
        SearchViewModel(get(), get())
    }
}
package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.ui.config.view_model.ConfigViewModel
import com.practicum.playlistmaker.ui.main.view_model.MainViewModel
import com.practicum.playlistmaker.ui.media.view_model.FavoriteFragmentViewModel
import com.practicum.playlistmaker.ui.media.view_model.PlaylistFragmentViewModel
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    factory {
        MediaPlayer()
    }

    viewModel{
        ConfigViewModel(get(), get())

    }
    viewModel{(url: String, trackTimeMillis: Int) ->
        PlayerViewModel(url, trackTimeMillis, get())
    }

    viewModel{
        SearchViewModel(get(), get())
    }

    viewModel{
        MainViewModel(get())
    }

    viewModel{
        FavoriteFragmentViewModel()
    }

    viewModel{
        PlaylistFragmentViewModel()
    }
}
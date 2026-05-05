package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.common.view_model.ThemeViewModel
import com.practicum.playlistmaker.ui.media.compose_ui.FavoriteUi
import com.practicum.playlistmaker.ui.media.view_model.FavoriteFragmentViewModel
import com.practicum.playlistmaker.ui.player.fragments.AudioPlayerFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment: Fragment() {
    private val viewModel: FavoriteFragmentViewModel by viewModel()
    private val viewModelTheme: ThemeViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = content {
        FavoriteUi(viewModel, viewModelTheme, onNavigateToAudioPlayer = { track ->
            showAudioPlayer(track)
        })
    }

    private fun showAudioPlayer(track: Track){
        findNavController().navigate(R.id.action_mediaFragment_to_audioPlayerFragment, AudioPlayerFragment.createArgs(track))
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}
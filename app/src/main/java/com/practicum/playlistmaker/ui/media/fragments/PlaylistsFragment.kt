package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.common.view_model.ThemeViewModel
import com.practicum.playlistmaker.ui.media.compose_ui.PlaylistsUi
import com.practicum.playlistmaker.ui.media.view_model.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {
    private val viewModel: PlaylistsFragmentViewModel by viewModel()
    private val viewModelTheme: ThemeViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = content {
        PlaylistsUi(viewModel, viewModelTheme,
            onNavigateToPlaylist = { playlist ->
                findNavController()
                    .navigate(
                        R.id.action_mediaFragment_to_playlistFragment,
                        PlaylistFragment.createArgs(playlist.id)
                    )
            },
            onNavigateToCreatePlaylist = {
                findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment)
            })
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}
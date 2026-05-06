package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.common.view_model.ThemeViewModel
import com.practicum.playlistmaker.ui.media.compose_ui.MediaUi
import com.practicum.playlistmaker.ui.media.view_model.FavoriteFragmentViewModel
import com.practicum.playlistmaker.ui.media.view_model.PlaylistsFragmentViewModel
import com.practicum.playlistmaker.ui.player.fragments.AudioPlayerFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MediaFragment : Fragment() {
    private val favoriteViewModel: FavoriteFragmentViewModel by viewModel()
    private val themeViewModel: ThemeViewModel by viewModel()
    private val playlistsViewModel: PlaylistsFragmentViewModel by viewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = content {
        MediaUi(favoriteViewModel, themeViewModel, playlistsViewModel,
            onNavigateToAudioPlayer = { track ->
                showAudioPlayer(track)
            },
            onNavigateToPlaylist = { playlist ->
                showPlaylist(playlist)
            },
            onNavigateToCreatePlaylist = {
                createPlaylist()
            }
        )
    }

    private fun showAudioPlayer(track: Track){
        findNavController().navigate(R.id.action_mediaFragment_to_audioPlayerFragment, AudioPlayerFragment.createArgs(track))
    }

    private fun showPlaylist(playlist: Playlist){
        findNavController()
            .navigate(
                R.id.action_mediaFragment_to_playlistFragment,
                PlaylistFragment.createArgs(playlist.id)
            )
    }

    private fun createPlaylist(){
        findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment)
    }
}
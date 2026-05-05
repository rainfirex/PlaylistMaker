package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaBinding
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

//    private var _binding: FragmentMediaBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var tabsMediator: TabLayoutMediator

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

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View?{
//        _binding = FragmentMediaBinding.inflate(inflater, container, false)
//        return binding.root
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.viewPager.adapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)
//        tabsMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, pos ->
//            when(pos){
//                0 -> tab.text = getString(R.string.tab_favorite)
//                1 -> tab.text = getString(R.string.tab_playlist)
//            }
//        }
//
//        tabsMediator.attach()
//    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        tabsMediator.detach()
//        _binding = null;
//    }
}
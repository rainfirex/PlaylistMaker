package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.ui.media.PlaylistAdaptor
import com.practicum.playlistmaker.ui.common.models.PlaylistsState
import com.practicum.playlistmaker.ui.media.view_model.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!


    private val viewModel: PlaylistsFragmentViewModel by viewModel()

    private val adaptor = PlaylistAdaptor(onItemClick = { position, playlist ->
        findNavController().navigate(R.id.action_mediaFragment_to_playlistFragment, PlaylistFragment.createArgs(playlist.id))
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment)
        }

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        binding.rvPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPlaylists.adapter = adaptor

        viewModel.getPlaylists()
    }

    private fun renderState(state: PlaylistsState){
        when(state){
            is PlaylistsState.Playlists -> viewState(state.playLists)
        }
    }

    private fun viewState(playLists: List<Playlist>){
        adaptor.data.clear()
        adaptor.data.addAll(playLists)
        adaptor.notifyDataSetChanged()

        if(adaptor.data.isNotEmpty()){
            binding.layoutDefault.isVisible = false
            binding.rvPlaylists.isVisible = true
        }
        else{
            binding.layoutDefault.isVisible = true
            binding.rvPlaylists.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}
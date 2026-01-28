package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.media.FavoriteTrackAdaptor
import com.practicum.playlistmaker.ui.media.models.FavoriteState
import com.practicum.playlistmaker.ui.media.view_model.FavoriteFragmentViewModel
import com.practicum.playlistmaker.ui.player.fragments.AudioPlayerFragment
import com.practicum.playlistmaker.ui.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment: Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteFragmentViewModel by viewModel()

    private val adaptor = FavoriteTrackAdaptor(onItemClick = { position, track ->
        onClickTrackDebounce(track)
    })

    private lateinit var onClickTrackDebounce: (Track) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickTrackDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false){ track ->
            showAudioPlayer(track)
        }

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        binding.rvTrack.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTrack.adapter = adaptor

        viewModel.getTracks()
    }

    private fun renderState(state: FavoriteState){
        when(state){
            is FavoriteState.Tracks -> stateTracks(state.tracks)
        }
    }

    private fun stateTracks(tracks: List<Track>){
        adaptor.data.clear()
        adaptor.data.addAll(tracks)
        adaptor.notifyDataSetChanged()

        if(adaptor.data.size > 0){
            binding.layoutDefault.isVisible = false
            binding.rvTrack.isVisible = true
        }
        else{
            binding.layoutDefault.isVisible = true
            binding.rvTrack.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAudioPlayer(track: Track){
        findNavController().navigate(R.id.action_mediaFragment_to_audioPlayerFragment, AudioPlayerFragment.createArgs(track))
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = FavoriteFragment()
    }
}
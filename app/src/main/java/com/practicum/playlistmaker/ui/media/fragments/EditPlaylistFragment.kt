package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.ui.common.models.DataState
import com.practicum.playlistmaker.ui.media.view_model.EditPlaylistFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment: CreatePlaylistFragment() {

    override val viewModel: EditPlaylistFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = requireArguments().getInt(PLAYLIST_ID_KEY)

        viewModel.getPlaylist(playlistId)

        binding.btnBack.setTitle(R.string.edit_playlist)
        binding.btnCreatePlaylist.text = getString(R.string.btn_edit_playlist)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() { findNavController().navigateUp() }
        } )

        binding.btnCreatePlaylist.setOnClickListener {
            val namePlaylist = binding.edtTitle.text.trim().toString()
            val description = binding.edtDescription.text.trim().toString()

            updatePlaylist(namePlaylist, description)
        }

        viewModel.observeStatePlaylist().observe(viewLifecycleOwner) { state ->
            when(state){
                is DataState.DataPlaylist -> initPlaylist(state.playlist)
                else -> {}
            }
        }
    }

    private fun updatePlaylist(namePlaylist: String, description: String){
        var path: String? = saveImage(namePlaylist)
        viewModel.updatePlaylist(namePlaylist, description, path)
        findNavController().navigateUp()
    }

    private fun initPlaylist(playlist: Playlist){
        binding.edtTitle.setText(playlist.namePlaylist)
        binding.edtDescription.setText(playlist.description)
        Glide.with(requireContext())
            .load(playlist.pathImage)
            .centerCrop()
            .placeholder(R.drawable.ic_album_image_placeholder_312)
            .into(binding.imageView)
    }

    override fun onDestroyView(){
        super.onDestroyView()
    }

    companion object {
        private const val PLAYLIST_ID_KEY = "playlistId"

        fun createArgs(playlistId: Int) : Bundle = bundleOf(
            PLAYLIST_ID_KEY to playlistId
        )
    }
}
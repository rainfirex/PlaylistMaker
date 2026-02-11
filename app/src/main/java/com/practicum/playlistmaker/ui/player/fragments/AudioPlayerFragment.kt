package com.practicum.playlistmaker.ui.player.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.player.enums.StateMediaPlayer
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.utils.Helper
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class AudioPlayerFragment: Fragment() {

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var url: String
    private var trackTimeMillis: Int = 0

    private val viewModel: PlayerViewModel by viewModel{
        parametersOf(url, trackTimeMillis)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = requireArguments().getParcelable<Track>(TRACK_KEY) as Track
        url = track.previewUrl.toString()
        trackTimeMillis = track.trackTimeMillis

        viewModel.observeStateFavoriteTrack().observe(viewLifecycleOwner) {
            setFavoriteIco(it)
        }

        viewModel.observeStateMediaPlayer().observe(viewLifecycleOwner) {
            binding.trackTimer.text = it.timerTrack

            when(it.state){
                StateMediaPlayer.STATE_PREPARED -> {
                    binding.apply {
                        btnPlay.isEnabled = true
                        btnPlay.setImageResource(R.drawable.ic_play_83)
                    }
                }
                StateMediaPlayer.STATE_PAUSED -> {
                    binding.btnPlay.setImageResource(R.drawable.ic_play_83)
                }
                StateMediaPlayer.STATE_PLAYING -> {
                    binding.btnPlay.setImageResource(R.drawable.ic_pause_83)
                }
                StateMediaPlayer.STATE_DEFAULT -> {
                    binding.apply {
                        btnPlay.isEnabled = false
                        trackTimer.text = getString(R.string.loading)
                    }
                }
            }
        }

        initTrack(track)

        binding.apply {
            btnPlay.setOnClickListener{ viewModel.playbackControl() }
            btnBack.setNavigationOnClickListener { findNavController().navigateUp() }
            btnFavourite.setOnClickListener { onFavoriteClicked(track) }
            btnAdd.setOnClickListener {  }
        }
    }

    private fun initTrack(track: Track){
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackGenre.text = track.primaryGenreName
        binding.trackCountry.text = track.country

        viewModel.setFavoriteTrack(track.isFavorite)

        val roundedCorner = Helper.Companion.dpToPx(8f, requireContext())
        Glide.with(requireContext())
            .load(track.getCoverArtwork())
            .centerCrop()
            .transform(RoundedCorners(roundedCorner))
            .placeholder(R.drawable.ic_album_image_placeholder_312)
            .into(binding.imageTrack)

        binding.trackDuration.text = SimpleDateFormat("m:ss", Locale.getDefault()).format(track.trackTimeMillis)

        if(track.collectionName.isNotEmpty()){
            binding.trackAlbum.text = track.collectionName
        }
        else{
            binding.trackAlbum.isVisible = false
            binding.textViewTrackAlbumName.isVisible = false
        }

        val year = track.getReleaseYear()
        if(!year.isNullOrEmpty()){
            binding.trackReleaseDateValue.text = year
        }
        else{
            binding.trackReleaseDateValue.isVisible = false
            binding.trackReleaseDate.isVisible = false
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onFavoriteClicked(track: Track){
        viewModel.changeFavoriteTrack(track)
    }

    private fun setFavoriteIco(isFavorite: Boolean){
        val image = if(isFavorite) R.drawable.ic_btn_favourite_like_51 else R.drawable.ic_btn_favourite_51
        binding.btnFavourite.setImageResource(image)
    }

    companion object {
        const val TRACK_KEY = "track"

        fun createArgs(track: Track): Bundle = bundleOf(
            TRACK_KEY to track
        )
    }
}
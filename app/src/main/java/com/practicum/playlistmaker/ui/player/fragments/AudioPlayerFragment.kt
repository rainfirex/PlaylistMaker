package com.practicum.playlistmaker.ui.player.fragments

import android.Manifest
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.telecom.ConnectionService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.services.PlayMusicService
import com.practicum.playlistmaker.ui.common.models.PlaylistsState
import com.practicum.playlistmaker.ui.player.PlaylistAdaptor
import com.practicum.playlistmaker.ui.player.enums.StateMediaPlayer
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.utils.ChangeInternetBroadcastReceiver
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
    private lateinit var track: Track

    private val adaptor = PlaylistAdaptor(onItemClick = { position, playlist ->
        viewModel.addTrack(playlist, track)
    })

    private val viewModel: PlayerViewModel by viewModel{
        parametersOf(url, trackTimeMillis)
    }

    private val internetBroadcastReceiver = ChangeInternetBroadcastReceiver()

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

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        viewModel.getPlaylist()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> { binding.overlay.isVisible = true }
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { binding.overlay.alpha = slideOffset + 0.7f}
        })

        track = requireArguments().getParcelable<Track>(TRACK_KEY) as Track
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
                        btnPlay.setPlaybackState(false)
                    }
                }
                StateMediaPlayer.STATE_PAUSED -> {
                    binding.btnPlay.setPlaybackState(false)
                }
                StateMediaPlayer.STATE_PLAYING -> {
                    binding.btnPlay.setPlaybackState(true)
                }
                StateMediaPlayer.STATE_DEFAULT -> {
                    binding.apply {
                        btnPlay.isEnabled = false
                        btnPlay.setPlaybackState(false)
                        trackTimer.text = getString(R.string.loading)
                    }
                }
            }
        }

        viewModel.observeStatePlaylists().observe(viewLifecycleOwner){
            when(it){
                is PlaylistsState.Playlists -> initPlaylists(it.playLists)
            }
        }

        viewModel.observeStateAddTrackToPlaylist().observe(viewLifecycleOwner) { result ->
            val status = result.first
            val playlistName = result.second
            var message: String
            if(status > 0){
                message =getString(R.string.track_add_playlist_success, playlistName)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
            else{
                message = getString(R.string.track_add_playlist_fail, playlistName)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }

        initTrack(track)

        binding.apply {
            rvPlaylists.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvPlaylists.adapter = adaptor

//            btnPlay.setOnClickListener{ viewModel.playbackControl() }
            btnBack.setNavigationOnClickListener { findNavController().navigateUp() }
            btnFavourite.setOnClickListener { onFavoriteClicked(track) }
            btnAdd.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            btnNewPlaylist.setOnClickListener {
                findNavController().navigate(R.id.action_audioPlayerFragment_to_createPlaylistFragment)
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        else{ bindMusicService() }
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

    private fun initPlaylists(playlist: List<Playlist>){
        adaptor.data.clear()
        adaptor.data.addAll(playlist)
        adaptor.notifyDataSetChanged()

        if(adaptor.data.isNotEmpty()){
            binding.rvPlaylists.isVisible = true
        }
        else{
            binding.rvPlaylists.isVisible = false
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
        requireContext().unregisterReceiver(internetBroadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            requireContext(),
            internetBroadcastReceiver,
            IntentFilter(ACTION_CONNECTIVITY_CHANGE),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onDestroyView() {
        unbindMusicService()
        super.onDestroyView()
        _binding = null
    }

    private fun onFavoriteClicked(track: Track){
        viewModel.changeFavoriteTrack(track)
    }

    private fun setFavoriteIco(isFavorite: Boolean){
        val image = if(isFavorite) R.drawable.ic_btn_favourite_like_51 else R.drawable.ic_btn_favourite_51
        binding.btnFavourite.setImageResource(image)
        track = track.copy(isFavorite = isFavorite)
    }

    ///region PlayMusicService

    private var playMusicService: PlayMusicService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayMusicService.PlayMusicServiceBinder
            playMusicService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            playMusicService = null
        }
    }

    private fun bindMusicService() {
        val intent = Intent(context, PlayMusicService::class.java)
        intent.putExtra(SONG_URL, url)
        requireContext().bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    private fun unbindMusicService() {
        requireContext().unbindService(serviceConnection)
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) { bindMusicService() }
        else { Toast.makeText(requireContext(), "Can't bind service!", Toast.LENGTH_LONG).show() }
    }

    ///endregion

    companion object {
        private const val ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"
        const val TRACK_KEY = "track"
        const val SONG_URL = "song_url"

        fun createArgs(track: Track): Bundle = bundleOf(
            TRACK_KEY to track
        )
    }
}
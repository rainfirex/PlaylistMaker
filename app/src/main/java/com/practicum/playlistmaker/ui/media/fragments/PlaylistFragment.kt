package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.common.data_adaptors.TrackAdaptor
import com.practicum.playlistmaker.ui.common.models.DataState
import com.practicum.playlistmaker.ui.media.view_model.PlaylistFragmentViewModel
import com.practicum.playlistmaker.ui.player.fragments.AudioPlayerFragment
import com.practicum.playlistmaker.ui.utils.Helper
import com.practicum.playlistmaker.ui.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class PlaylistFragment: Fragment()  {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistFragmentViewModel by viewModel()

    private lateinit var onClickTrackDebounce: (Pair<Track, Int>) -> Unit

    private val trackAdaptor = TrackAdaptor(
        onItemClick = { position, track -> onClickTrackDebounce(Pair(track, position)) },
        onItemLongClick =  { position, track -> showRemoveTrackDialog(track) }
    )

    var playlist: Playlist? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvTracks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTracks.adapter = trackAdaptor

        onClickTrackDebounce = debounce<Pair<Track,Int>>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false){ pair ->
            showAudioPlayer(pair.first)
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.BottomSheetMenu)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        if (playlist != null){

                            val roundedCorner = Helper.Companion.dpToPx(ROUNDED, requireContext())

                            Glide.with(requireContext())
                                .load(playlist?.pathImage)
                                .transform(MultiTransformation(
                                    CenterCrop(),
                                    RoundedCorners(roundedCorner)
                                ))
                                .placeholder(R.drawable.ic_album_image_placeholder_312)
                                .into(binding.imgPlaylist)



                            binding.txtNamePlaylist.text = playlist?.namePlaylist
                            binding.txtPlaylistCount.text = resources.getQuantityString(R.plurals.view_track_with_d, playlist!!.count, playlist?.count, playlist?.count)
                        }

                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }
                    else -> { binding.overlay.isVisible = true }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { binding.overlay.alpha = slideOffset + 0.7f}
        })

        val playlistId = requireArguments().getInt(PLAYLIST_ID_KEY)

        viewModel.observeStatePlaylist().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        viewModel.observeStateTracks().observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        viewModel.observeStateRemovePlaylist().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.imgShared.setOnClickListener { sharedPlaylist() }

        binding.imgMenu.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.txtShared.setOnClickListener { sharedPlaylist() }

        binding.txtEditPlaylist.setOnClickListener { findNavController().navigate(R.id.action_playlistFragment_to_editPlaylistFragment, EditPlaylistFragment.createArgs(playlistId)) }

        binding.txtRemovePlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            showRemovePlaylistDialog()
        }

        viewModel.getPlaylist(playlistId)
    }

    private fun showAudioPlayer(track: Track){
        findNavController()
            .navigate(R.id.action_playlistFragment_to_audioPlayerFragment, AudioPlayerFragment.createArgs(track))
    }

    private fun showRemovePlaylistDialog(){
        val localPlaylist = playlist
        if(localPlaylist != null){
            val drawableBackground  = AppCompatResources.getDrawable(requireContext(), R.drawable.shape_material_alert_dialog)
            MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog)
                .setBackground(drawableBackground)
                .setTitle(R.string.alert_dialog_playlist_remove_title)
                .setMessage(getString(R.string.alert_dialog_playlist_remove_body, localPlaylist.namePlaylist))
                .setPositiveButton(R.string.alert_dialog_remove_btn_positive){ dialog, which ->
                    viewModel.removePlaylist(localPlaylist)}
                .setNegativeButton(R.string.alert_dialog_remove_btn_negative){ dialog, which -> }
                .show()
        }
    }

    private fun showRemoveTrackDialog(track: Track){
        val localPlaylist = playlist
        if(localPlaylist != null) {
            val drawableBackground  = AppCompatResources.getDrawable(requireContext(), R.drawable.shape_material_alert_dialog)
            MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog)
                .setBackground(drawableBackground)
                .setTitle(R.string.alert_dialog_track_remove_title)
                .setPositiveButton(R.string.alert_dialog_remove_btn_positive){ dialog, which -> viewModel.removeTrackFromPlaylist(localPlaylist, track.trackId)}
                .setNegativeButton(R.string.alert_dialog_remove_btn_negative){ dialog, which -> }
                .show()
        }
    }

    private fun renderState(state: DataState){
        when(state){
            is DataState.DataPlaylist -> initPlaylist(state.playlist)
            is DataState.DataTracks -> initTracks(state.tracks)
            else ->  { }
        }
    }

    private fun initPlaylist(playlist: Playlist){
        this.playlist = playlist

        Glide.with(requireContext())
            .load(playlist.pathImage)
            .centerCrop()
            .placeholder(R.drawable.ic_album_image_placeholder_312)
            .into(binding.ImagePlaylist)

        binding.txtTitle.text = playlist.namePlaylist
        binding.txtDescription.text = playlist.description
        binding.txtTrackCount.text = binding.txtTrackCount.resources.getQuantityString(R.plurals.view_track_with_d, playlist.count, playlist.count, playlist.count)
        binding.txtTime.text = resources.getQuantityString(R.plurals.view_time_with_d, 0)
    }

    private fun initTracks(tracks: List<Track>){
        var time = 0;
        tracks.forEach { track ->
            time += track.trackTimeMillis
        }

        val timeFormatted = SimpleDateFormat("mm", Locale.getDefault()).format(time)
        val min = timeFormatted.toInt()

        binding.txtTime.text = resources.getQuantityString(R.plurals.view_time_with_d, min, min,min, min)

        if(tracks.isNotEmpty()){
            trackAdaptor.data.clear()
            trackAdaptor.data.addAll(tracks)
            trackAdaptor.notifyDataSetChanged()
        }
        else{
            Toast.makeText(requireContext(), getString(R.string.toast_playlist_no_tracks2), Toast.LENGTH_SHORT).show()
        }
    }

    private fun sharedPlaylist(){
        val pl = playlist
        val tracks = trackAdaptor.data.toList()
        if(tracks.isNotEmpty() && pl != null){
            trackAdaptor.data
            viewModel.sharePlaylist(pl, tracks)
        }
        else{
            Toast.makeText(requireContext(), R.string.toast_playlist_no_tracks, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ROUNDED = 2f
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val PLAYLIST_ID_KEY = "playlistId"

        fun createArgs(playlistId: Int) : Bundle = bundleOf(
            PLAYLIST_ID_KEY to playlistId
        )
    }
}
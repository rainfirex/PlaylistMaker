package com.practicum.playlistmaker.ui.player.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.player.enums.StateMediaPlayer
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.utils.Helper
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
//    private lateinit var viewModel: PlayerViewModel

    private lateinit var url: String
    private var trackTimeMillis: Int = 0

    private val viewModel: PlayerViewModel by viewModel{
        parametersOf(url, trackTimeMillis)
    }


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        val intent: Intent = intent
        val track = intent.getParcelableExtra<Track>(TRACK_KEY) as Track

        url = track.previewUrl.toString()
        trackTimeMillis = track.trackTimeMillis

//        viewModel = ViewModelProvider(this, PlayerViewModel.getFactory(track.previewUrl.toString(), track.trackTimeMillis))
//            .get(PlayerViewModel::class.java)

        viewModel.observePlayerState().observe(this) {
            when(it){
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
        viewModel.observeTimerTrack().observe(this){
            binding.trackTimer.text = it
        }

        initTrack(track)

        binding.apply {
            btnPlay.setOnClickListener{ viewModel.playbackControl() }
            btnBack.setNavigationOnClickListener { finish() }
        }
    }

    private fun initTrack(track: Track){
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackGenre.text = track.primaryGenreName
        binding.trackCountry.text = track.country

        val roundedCorner = Helper.Companion.dpToPx(8f, applicationContext)
        Glide.with(applicationContext)
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

    companion object{
        const val TRACK_KEY = "track"
    }
}
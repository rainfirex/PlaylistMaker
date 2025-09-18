package com.practicum.playlistmaker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.models.Track
import com.practicum.playlistmaker.utils.Helper
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.audio_player)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        val intent: Intent = intent
        val track = intent.getParcelableExtra<Track>(TRACK_KEY) as Track
//        val strTrack = intent.getStringExtra(TRACK_KEY)
//        val gson = Gson()
//        val track = gson.fromJson(strTrack, Track::class.java)

        val btnBack = findViewById<Toolbar>(R.id.toolbar)
        btnBack.setNavigationOnClickListener { finish() }

        val trackName = findViewById<TextView>(R.id.textViewTrackName)
        val artistName = findViewById<TextView>(R.id.textViewArtistName)
        val imageTrack = findViewById<ImageView>(R.id.imageTrack)
        val trackTimer = findViewById<TextView>(R.id.textViewTrackTimer)
        val trackDuration = findViewById<TextView>(R.id.textViewTrackDurationValue)
        val trackAlbum = findViewById<TextView>(R.id.textViewTrackAlbumNameValue)
        val trackReleaseDate = findViewById<TextView>(R.id.textViewTrackReleaseDateValue)
        val trackGenre = findViewById<TextView>(R.id.textViewTrackGenreValue)
        val trackCountry = findViewById<TextView>(R.id.textViewTrackCountryValue)

        val textViewTrackAlbumName = findViewById<TextView>(R.id.textViewTrackAlbumName)
        val textViewTrackReleaseDate = findViewById<TextView>(R.id.textViewTrackReleaseDate)

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTimer.text = SimpleDateFormat("m:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val roundedCorner = Helper.dpToPx(8f, applicationContext)

        Glide.with(applicationContext)
            .load(track.getCoverArtwork())
            .centerCrop()
            .transform(RoundedCorners(roundedCorner))
            .placeholder(R.drawable.ic_album_image_placeholder_312)
            .into(imageTrack)

        trackDuration.text = SimpleDateFormat("m:ss", Locale.getDefault()).format(track.trackTimeMillis)

        if(track.collectionName.isNotEmpty()){
            trackAlbum.text = track.collectionName
        }
        else{
            trackAlbum.isVisible = false
            textViewTrackAlbumName.isVisible = false
        }

        val year = track.getReleaseYear()
        if(!year.isNullOrEmpty()){
            trackReleaseDate.text = year
        }
        else{
            trackReleaseDate.isVisible = false
            textViewTrackReleaseDate.isVisible = false
        }

        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country
    }

    companion object{
        const val TRACK_KEY = "track"
    }
}
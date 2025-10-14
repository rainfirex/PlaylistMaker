package com.practicum.playlistmaker.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.models.Track
import com.practicum.playlistmaker.utils.Helper
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity: AppCompatActivity() {

    private var playerState = STATE_DEFAULT

    private var mediaPlayer = MediaPlayer()

    private lateinit var btnPlay: ImageView
    private lateinit var trackTimer: TextView

    private val handler = Handler(Looper.getMainLooper())

    private val timerTaskRunnable = timerTask()

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
        trackTimer = findViewById(R.id.textViewTrackTimer)
        val trackDuration = findViewById<TextView>(R.id.textViewTrackDurationValue)
        val trackAlbum = findViewById<TextView>(R.id.textViewTrackAlbumNameValue)
        val trackReleaseDate = findViewById<TextView>(R.id.textViewTrackReleaseDateValue)
        val trackGenre = findViewById<TextView>(R.id.textViewTrackGenreValue)
        val trackCountry = findViewById<TextView>(R.id.textViewTrackCountryValue)

        val textViewTrackAlbumName = findViewById<TextView>(R.id.textViewTrackAlbumName)
        val textViewTrackReleaseDate = findViewById<TextView>(R.id.textViewTrackReleaseDate)

        btnPlay = findViewById(R.id.imagePlay)

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

        btnPlay.setOnClickListener{
            playbackControl()
        }

        preparePlayer(track.previewUrl)
    }

    private fun timerTask(): Runnable {
        return object: Runnable{
            override fun run(){
                trackTimer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, DELAY)
            }
        }
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            btnPlay.isEnabled = true
            playerState = STATE_PREPARED
            trackTimer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
        }
        mediaPlayer.setOnCompletionListener {
            btnPlay.setImageResource(R.drawable.ic_play_83)
            playerState = STATE_PREPARED

            handler.removeCallbacks( timerTaskRunnable )
            trackTimer.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
        }
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        btnPlay.setImageResource(R.drawable.ic_pause_83)
        playerState = STATE_PLAYING
        handler.post(timerTaskRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        btnPlay.setImageResource(R.drawable.ic_play_83)
        playerState = STATE_PAUSED
        handler.removeCallbacks( timerTaskRunnable )
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks( timerTaskRunnable )
    }

    companion object{
        const val TRACK_KEY = "track"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
    }
}
package com.practicum.playlistmaker.ui.media

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.utils.Helper
import java.text.SimpleDateFormat
import java.util.Locale

class FavoriteTrackViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val layout = view.findViewById<LinearLayout>(R.id.layout)
    private val imgCover = view.findViewById<ImageView>(R.id.ImageCover)
    //    private val imgForward = view.findViewById<ImageView>(R.id.ImageForward)
    private val tvTrack = view.findViewById<TextView>(R.id.TextViewTrack)
    private val tvInfoArtist = view.findViewById<TextView>(R.id.TextViewInfoArtist)
    private val tvInfoTime = view.findViewById<TextView>(R.id.TextViewInfoTime)
    private val roundedCorner = Helper.Companion.dpToPx(2f, layout.context)

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_track, parent, false)
    )

    fun bind(track: Track){
        if (track.trackName.isNullOrEmpty() || track.artistName.isNullOrEmpty()) return

        tvTrack.text = track.trackName.trim()
        tvInfoArtist.text = track.artistName.trim()
        tvInfoTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        Glide.with(layout)
            .load(track.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(roundedCorner))
            .placeholder(R.drawable.ic_track_placeholder_45)
            .into(imgCover)
    }
}
package com.practicum.playlistmaker.ui.media

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.ui.utils.Helper

class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val layout = view.findViewById<LinearLayout>(R.id.layout)
    private val imgPlaylist = view.findViewById<ImageView>(R.id.imgPlaylist)
    private val txtNamePlaylist = view.findViewById<TextView>(R.id.txtNamePlaylist)
    private val txtPlaylistCount = view.findViewById<TextView>(R.id.txtPlaylistCount)
    private val roundedCorner = Helper.Companion.dpToPx(ROUNDED, layout.context)

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grid_playlist, parent, false)
    )

    fun bind(playlist: Playlist){
        if (playlist.namePlaylist.isEmpty()) return

        txtNamePlaylist.text = playlist.namePlaylist
        txtPlaylistCount.text = txtPlaylistCount.resources.getQuantityString(R.plurals.view_track_with_d, playlist.count, playlist.count, playlist.count)

        Glide.with(layout)
            .load(playlist.pathImage)
            .transform(MultiTransformation(
                CenterCrop(),
                RoundedCorners(roundedCorner)
            ))
            .placeholder(R.drawable.ic_track_placeholder_45)
            .into(imgPlaylist)
    }

    companion object{
        private const val ROUNDED = 8f
    }
}
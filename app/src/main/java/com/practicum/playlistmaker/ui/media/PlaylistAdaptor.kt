package com.practicum.playlistmaker.ui.media

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.models.Playlist

class PlaylistAdaptor(private val onItemClick: ( (Int, Playlist) -> Unit)? = null):  RecyclerView.Adapter<PlaylistViewHolder>() {

    var data = mutableListOf<Playlist>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder {
        return PlaylistViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int
    ) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position, data[position])
        }
    }

    override fun getItemCount(): Int = data.size
}
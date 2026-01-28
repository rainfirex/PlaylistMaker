package com.practicum.playlistmaker.ui.media

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.models.Track

class FavoriteTrackAdaptor(private val onItemClick: ( (Int, Track) -> Unit)? = null) : RecyclerView.Adapter<FavoriteTrackViewHolder>(){
    var data = mutableListOf<Track>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteTrackViewHolder {
        return FavoriteTrackViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: FavoriteTrackViewHolder,
        position: Int
    ) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(position, data[position])
        }
    }

    override fun getItemCount(): Int = data.size
}
package com.practicum.playlistmaker.ui.common.data_adaptors

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.models.Track

class TrackAdaptor(
    private val onItemClick: ((Int, Track) -> Unit)? = null,
    private val onItemLongClick: ((Int, Track) -> Unit)? = null
) : RecyclerView.Adapter<TrackViewHolder>(){

    var data = mutableListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(position, data[position])
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(position, data[position])
            true
        }
    }
}
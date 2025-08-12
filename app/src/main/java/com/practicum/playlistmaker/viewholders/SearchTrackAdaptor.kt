package com.practicum.playlistmaker.viewholders

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.models.Track

class SearchTrackAdaptor(private val data: List<Track>) : RecyclerView.Adapter<SearchTrackViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        return SearchTrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
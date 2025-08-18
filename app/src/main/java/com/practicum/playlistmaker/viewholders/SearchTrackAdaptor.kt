package com.practicum.playlistmaker.viewholders

import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.models.Track

class SearchTrackAdaptor : RecyclerView.Adapter<SearchTrackViewHolder>(){

    var data = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        return SearchTrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener{
            Toast.makeText(holder.itemView.context, data[position].trackName, Toast.LENGTH_SHORT).show()
        }
    }
}
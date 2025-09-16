package com.practicum.playlistmaker.services

import android.app.Application.MODE_PRIVATE
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.models.Track
import androidx.core.content.edit
import com.practicum.playlistmaker.viewholders.SearchTrackAdaptor

class SearchHistoryService(ctx: Context, val onItemClick: ( (Int, Track) -> Unit)? = null) {
    private var sharedPrefs: SharedPreferences = ctx.getSharedPreferences(HISTORY_DATA_FILE, MODE_PRIVATE)
    private val gson = Gson()
    private val historyAdaptor = SearchTrackAdaptor(onItemClick)
    private val tracks = load()

    init {
        historyAdaptor.data = tracks
    }

    private fun load(): MutableList<Track> {
        val data = sharedPrefs.getString(HISTORY_LIST, null)
        if(data.isNullOrEmpty()){
            return mutableListOf()
        }
        else{
            val listType = object : TypeToken<MutableList<Track>>() {}.type
            return gson.fromJson(data, listType)
        }
    }

    fun add(track: Track, position: Int) {
        val existTrack = tracks.firstOrNull { it -> it.trackId == track.trackId }
        if(existTrack != null){
            //val position = tracks.indexOf(existTrack)
            tracks.remove(existTrack)
            historyAdaptor.notifyItemRemoved(position)
        }
        else if(tracks.size == 10) {
            val lastPosition = tracks.size-1
            tracks.removeAt(lastPosition)
            historyAdaptor.notifyItemRemoved(lastPosition)
        }

        val indexInsert = 0
        tracks.add(indexInsert, track)

        historyAdaptor.notifyItemInserted(indexInsert)
        historyAdaptor.notifyItemRangeChanged(indexInsert, tracks.size)
    }

    fun save(){
        val data = gson.toJson(tracks)
        sharedPrefs.edit() { putString(HISTORY_LIST, data) }
    }

    fun getAdaptor(): SearchTrackAdaptor {
        return historyAdaptor
    }

    fun clear() {
        tracks.clear()
        historyAdaptor.notifyDataSetChanged()
    }

    fun countItems(): Int{
        return historyAdaptor.data.size
    }

    companion object{
        private const val HISTORY_DATA_FILE = "history_data"
        private const val HISTORY_LIST = "history_list"
    }
}
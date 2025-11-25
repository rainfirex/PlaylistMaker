package com.practicum.playlistmaker.data.search.db

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.search.dto.TrackDto
import com.practicum.playlistmaker.data.search.DataStorage

class TracksHistoryDataStorage(private var sharedPrefs: SharedPreferences, private val gson: Gson): DataStorage {

    override fun save(tracks: MutableList<TrackDto>) {
        val data = gson.toJson(tracks)
        sharedPrefs.edit() { putString(HISTORY_LIST, data) }
    }

    override fun load(): MutableList<TrackDto> {
        val data = sharedPrefs.getString(HISTORY_LIST, null)
        if(data.isNullOrEmpty()){
            return mutableListOf()
        }
        else{
            val listType = object : TypeToken<MutableList<TrackDto>>() {}.type
            return gson.fromJson(data, listType)
        }
    }

    companion object{
        private const val HISTORY_LIST = "history_list"
    }
}
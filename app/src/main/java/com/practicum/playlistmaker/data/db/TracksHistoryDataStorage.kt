package com.practicum.playlistmaker.data.db

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.DataStorage
import com.practicum.playlistmaker.data.dto.TrackDto


class TracksHistoryDataStorage(ctx: Context): DataStorage {

    private var sharedPrefs: SharedPreferences = ctx.getSharedPreferences(HISTORY_DATA_FILE, MODE_PRIVATE)
    private val gson = Gson()

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
        private const val HISTORY_DATA_FILE = "history_data"
        private const val HISTORY_LIST = "history_list"
    }
}
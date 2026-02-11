package com.practicum.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Parcelize
data class Track (
    val trackId: Int, val trackName: String, val artistName: String,
    val trackTimeMillis: Int, val artworkUrl100: String,
    val collectionName: String, val releaseDate: String,
    val primaryGenreName: String, val country: String, val previewUrl: String, val isFavorite: Boolean = false) : Parcelable {

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    fun getReleaseYear(): String? {
        val format = SimpleDateFormat("yyyy", Locale.getDefault())
        val dateTime = format.parse(releaseDate)
        if (dateTime != null) {
            val calendar = Calendar.getInstance()
            calendar.time = dateTime
            return calendar.get(Calendar.YEAR).toString()
        }
        return null
    }
}

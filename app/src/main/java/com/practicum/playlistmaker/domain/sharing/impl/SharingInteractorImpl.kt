package com.practicum.playlistmaker.domain.sharing.impl

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Playlist
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class SharingInteractorImpl(private val externalNavigator: SharingRepository, private val ctx: Context): SharingInteractor {

    override fun shareApp(url: String) {
        externalNavigator.shareLink(url)
    }

    override fun openTerms(url: String) {
        externalNavigator.openLink(url)
    }

    override fun openSupport(subject: String, message: String) {
        externalNavigator.openEmail(subject, message)
    }

    override fun sharePlaylist(playlist: Playlist, tracks: List<Track>) {
        val text = createText(playlist.namePlaylist, playlist.description, playlist.count, tracks)
        externalNavigator.sharePlaylist(text)
    }

    private fun createText(namePlaylist: String, description: String?, tracksCount: Int, tracks: List<Track>): String{
        return buildString{
            appendLine(namePlaylist)
            if (description != null && description.isNotEmpty()){
                appendLine(description)
            }

            val tracksWord = ctx.resources.getQuantityString(R.plurals.view_track_with_02d, tracksCount, tracksCount, tracksCount)
            appendLine(tracksWord)

            tracks.forEachIndexed { index, track ->
                val trackNumber = index + 1
                val time = SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(track.trackTimeMillis)
                appendLine("[$trackNumber]. [${track.artistName}] - [${track.trackName}] ([${time}])")
            }
        }
    }

    companion object{
        private const val TIME_FORMAT: String = "mm:ss"
    }
}
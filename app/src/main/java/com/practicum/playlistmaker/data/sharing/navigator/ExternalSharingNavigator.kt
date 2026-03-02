package com.practicum.playlistmaker.data.sharing.navigator

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharing.SharingNavigator
import com.practicum.playlistmaker.data.sharing.dto.EmailDataDto

class ExternalSharingNavigator(private val ctx: Context): SharingNavigator {

    override fun shareLink(url: String) {
        val intentShared = Intent(Intent.ACTION_SEND)
        intentShared.setType("text/plain")
        intentShared.putExtra(Intent.EXTRA_TEXT, url)

        ctx.startActivity(Intent.createChooser(intentShared, ctx.getString(R.string.shared_title))
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun openLink(url: String) {
        ctx.startActivity(Intent(Intent.ACTION_VIEW, url.toUri())
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    override fun openEmail(dto: EmailDataDto) {
        val intentSupport = Intent(Intent.ACTION_SENDTO)
        intentSupport.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentSupport.data = "mailto:".toUri()
        intentSupport.putExtra(Intent.EXTRA_EMAIL, arrayOf("thegodkot@yandex.ru"))
        intentSupport.putExtra(Intent.EXTRA_SUBJECT, dto.subject)
        intentSupport.putExtra(Intent.EXTRA_TEXT, dto.message)

        ctx.startActivity(intentSupport)
    }

    override fun sharePlaylist(text: String) {
        val intentShared = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }

        ctx.startActivity(Intent.createChooser(intentShared, ctx.getString(R.string.shared_title))
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
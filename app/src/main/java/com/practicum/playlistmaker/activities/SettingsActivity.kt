package com.practicum.playlistmaker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import androidx.core.net.toUri

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener{
            finish()
        }

        val itemMenuShared = findViewById<FrameLayout>(R.id.itemMenuShared)
        itemMenuShared.setOnClickListener{
            val intentShared = Intent(Intent.ACTION_SEND)
            intentShared.setType("text/plain")
            intentShared.putExtra(Intent.EXTRA_TEXT, getString(R.string.url_android_dev))

            startActivity(Intent.createChooser(intentShared, getString(R.string.shared_title)))
        }

        val itemMenuSupport = findViewById<FrameLayout>(R.id.itemMenuSupport)
        itemMenuSupport.setOnClickListener{
            val subject = getString(R.string.support_subject)
            val message = getString(R.string.support_message)

            val intentSupport = Intent(Intent.ACTION_SENDTO)
            intentSupport.data = "mailto:".toUri()
            intentSupport.putExtra(Intent.EXTRA_EMAIL, arrayOf("thegodkot@yandex.ru"))
            intentSupport.putExtra(Intent.EXTRA_SUBJECT, subject)
            intentSupport.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(intentSupport)
        }

        val itemMenuAgree = findViewById<FrameLayout>(R.id.itemMenuAgree)
        itemMenuAgree.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, getString(R.string.url_offer).toUri()))
        }
    }
}

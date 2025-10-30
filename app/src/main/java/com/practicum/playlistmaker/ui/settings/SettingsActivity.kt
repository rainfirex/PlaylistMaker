package com.practicum.playlistmaker.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {

    private val configProvider = Creator.providerConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.MainLayout)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        val btnBack = findViewById<Toolbar>(R.id.toolbar)
        btnBack.setNavigationOnClickListener{ finish() }

        val itemMenuShared = findViewById<MaterialTextView>(R.id.itemMenuShared)
        itemMenuShared.setOnClickListener{
            val intentShared = Intent(Intent.ACTION_SEND)
            intentShared.setType("text/plain")
            intentShared.putExtra(Intent.EXTRA_TEXT, getString(R.string.url_android_dev))

            startActivity(Intent.createChooser(intentShared, getString(R.string.shared_title)))
        }

        val itemMenuSupport = findViewById<MaterialTextView>(R.id.itemMenuSupport)
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

        val itemMenuAgree = findViewById<MaterialTextView>(R.id.itemMenuAgree)
        itemMenuAgree.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, getString(R.string.url_offer).toUri()))
        }

        val itemThemeSwitcher = findViewById<SwitchMaterial>(R.id.itemThemeSwitcher)
        itemThemeSwitcher.isChecked = configProvider.getTheme()
        itemThemeSwitcher.setOnCheckedChangeListener{ _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
            configProvider.setTheme(isChecked)
        }
    }
}
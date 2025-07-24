package com.practicum.playlistmaker.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val btnMedia = findViewById<Button>(R.id.btnMedia)
        val btnSetting = findViewById<Button>(R.id.btnSetting)

        btnSearch.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        val btnMediaListener: View.OnClickListener = View.OnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        btnMedia.setOnClickListener(btnMediaListener)

        btnSetting.setOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
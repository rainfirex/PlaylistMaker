package com.practicum.playlistmaker.ui.media

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MediaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.)) { view, insets ->
//            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
//            view.updatePadding(top = statusBar.top)
//            insets
//        }
    }
}
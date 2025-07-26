package com.practicum.playlistmaker.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.core.view.updatePadding
//import com.practicum.playlistmaker.R

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

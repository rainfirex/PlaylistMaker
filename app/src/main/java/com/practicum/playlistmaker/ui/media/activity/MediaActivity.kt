package com.practicum.playlistmaker.ui.media.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.ui.media.fragments.MediaViewPagerAdapter

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        binding.apply {
            btnBack.setNavigationOnClickListener {
                finish()
            }
        }

        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, pos ->
            when(pos){
                0 -> tab.text = getString(R.string.tab_favorite)
                1 -> tab.text = getString(R.string.tab_playlist)
            }
        }

        tabMediator.attach()
    }
}
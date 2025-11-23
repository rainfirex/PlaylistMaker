package com.practicum.playlistmaker.ui.config.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityConfigBinding
import com.practicum.playlistmaker.ui.config.view_model.ConfigViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfigBinding
//    private lateinit var viewModel: ConfigViewModel

    private val viewModel: ConfigViewModel  by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

//        viewModel = ViewModelProvider(this, ConfigViewModel.getFactory())
//            .get(ConfigViewModel::class.java)

        viewModel.loadTheme()

        binding.itemMenuShared.setOnClickListener{
            viewModel.shareApp(getString(R.string.url_android_dev))
        }

        binding.itemMenuSupport.setOnClickListener{
            val subject = getString(R.string.support_subject)
            val message = getString(R.string.support_message)
            viewModel.openSupport(subject, message)
        }

        binding.itemMenuAgree.setOnClickListener{
            viewModel.openTerms(getString(R.string.url_offer))
        }

        viewModel.observeIsDarkTheme().observe(this) {
            binding.itemThemeSwitcher.isChecked = it
        }

        binding.itemThemeSwitcher.setOnCheckedChangeListener{ _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
            viewModel.setTheme(isChecked)
        }

        binding.btnBack.setNavigationOnClickListener{ finish() }
    }
}
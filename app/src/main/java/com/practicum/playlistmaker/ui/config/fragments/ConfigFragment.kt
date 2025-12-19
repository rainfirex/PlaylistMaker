package com.practicum.playlistmaker.ui.config.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentConfigBinding
import com.practicum.playlistmaker.ui.config.view_model.ConfigViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class ConfigFragment: Fragment() {

    private lateinit var binding: FragmentConfigBinding

    private val viewModel: ConfigViewModel  by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            viewModel.setTheme(isChecked)
        }
    }
}
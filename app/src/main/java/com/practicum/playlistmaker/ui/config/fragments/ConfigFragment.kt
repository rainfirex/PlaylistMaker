package com.practicum.playlistmaker.ui.config.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import com.practicum.playlistmaker.ui.config.compose_ui.ConfigUi
import com.practicum.playlistmaker.ui.config.view_model.ConfigViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class ConfigFragment: Fragment() {
    private val viewModel: ConfigViewModel  by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = content {
        ConfigUi(viewModel)
    }
}
package com.practicum.playlistmaker.ui.search.fragments

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.common.view_model.ThemeViewModel
import com.practicum.playlistmaker.ui.player.fragments.AudioPlayerFragment
import com.practicum.playlistmaker.ui.search.compose_ui.SearchUi
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.utils.ChangeInternetBroadcastReceiver
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment: Fragment() {

    private val viewModel: SearchViewModel by viewModel()
    private val viewModelTheme: ThemeViewModel by viewModel()
    private val internetBroadcastReceiver = ChangeInternetBroadcastReceiver()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = content {
        SearchUi(viewModel, viewModelTheme, onNavigateToAudioPlayer = { track ->
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveHistory()
        requireContext().unregisterReceiver(internetBroadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            requireContext(),
            internetBroadcastReceiver,
            IntentFilter(ACTION_CONNECTIVITY_CHANGE),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    companion object{
        private const val ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE"
    }
}
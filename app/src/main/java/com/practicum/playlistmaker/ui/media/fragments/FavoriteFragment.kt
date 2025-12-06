package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavoriteBinding
import com.practicum.playlistmaker.ui.media.view_model.FavoriteFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment: Fragment() {
    private lateinit var binding: FragmentFavoriteBinding

    private val viewModel: FavoriteFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        private const val POSTER_URL = "poster_url"

        fun newInstance() = FavoriteFragment()
//            .apply {
//            arguments = Bundle().apply {
//                putString(POSTER_URL, posterUrl)
//            }
//        }
    }
}
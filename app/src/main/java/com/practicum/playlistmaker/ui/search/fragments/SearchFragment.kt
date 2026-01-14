package com.practicum.playlistmaker.ui.search.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.player.fragments.AudioPlayerFragment
import com.practicum.playlistmaker.ui.search.TrackAdaptor
import com.practicum.playlistmaker.ui.search.models.SearchState
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.utils.Helper
import com.practicum.playlistmaker.ui.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchFragment: Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

    private var textSearch: String = ""

    private var textWatcher: TextWatcher? = null

    private lateinit var onClickTrackDebounce: (Pair<Track, Int>) -> Unit

    private val searchAdaptor = TrackAdaptor(onItemClick = { position, track ->
        onClickTrackDebounce(Pair(track, position))
    })

    private val historyAdaptor = TrackAdaptor(onItemClick = { position, track ->
        onClickTrackDebounce(Pair(track, -1))
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickTrackDebounce = debounce<Pair<Track,Int>>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false){ pair ->
            if(pair.second != -1){
                viewModel.addHistory(pair.first, pair.second)
            }
            showAudioPlayer(pair.first)
        }

        viewModel.observeState().observe(viewLifecycleOwner){
            renderSearchState(it)
        }

        binding.buttonClearSearch.setOnClickListener{
            Helper.Companion.visibleKeyboard(binding.buttonClearSearch, false)

            binding.editTextSearch.text?.clear()

            searchAdaptor.data.clear()
            searchAdaptor.notifyDataSetChanged()

            binding.layoutFail.isVisible = false
            binding.rvTrack.isVisible = false

            if(historyAdaptor.data.isNotEmpty()){
                binding.layoutHistory.isVisible = true
            }
        }

        binding.btnClearHistory.setOnClickListener{
            viewModel.clearHistory()
            historyAdaptor.notifyDataSetChanged()
            binding.layoutHistory.isVisible = false
        }

        binding.btnSearchFailUpdate.setOnClickListener{
            viewModel.searchTracksDebounce(textSearch)
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable?) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textSearch = s?.toString() ?: ""

                binding.buttonClearSearch.isVisible = !textSearch.isEmpty()

                binding.layoutHistory.isVisible = textSearch.isEmpty()

                viewModel.searchTracksDebounce(textSearch)
            }
        }

        textWatcher?.let { binding.editTextSearch.addTextChangedListener(it) }

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchTracksDebounce(textSearch)
            }
            false
        }

        binding.editTextSearch.setOnFocusChangeListener{ view, hasFocus ->
            binding.layoutHistory.isVisible = (hasFocus && (view as EditText).text.isNullOrEmpty())
        }

        binding.editTextSearch.requestFocus()
        binding.editTextSearch.postDelayed({
            Helper.Companion.visibleKeyboard(binding.editTextSearch, true)
        }, 100)

        binding.rvTrack.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTrack.adapter = searchAdaptor

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvHistory.adapter = historyAdaptor

        if(searchAdaptor.data.count() > 0){
            binding.rvTrack.isVisible = true
        }
    }

    private fun renderSearchState(state: SearchState){
        when(state){
            is SearchState.Loading -> stateLoading()
            is SearchState.SearchResult -> stateData(state.tracks)
            is SearchState.Error -> stateError(state.stringId, state.drawableId)
            is SearchState.HistoryResult -> stateHistory(state.tracks)
        }
    }

    private fun stateLoading(){
        binding.rvTrack.isVisible = false
        binding.progressBar.isVisible = true
    }

    private fun stateData(tracks: List<Track>){
        searchAdaptor.data.clear()
        searchAdaptor.data.addAll(tracks)
        searchAdaptor.notifyDataSetChanged()
        binding.rvTrack.isVisible = true
        binding.progressBar.isVisible = false
    }

    private fun stateError(stringId: Int, drawable: Int){
        responseFailShow(stringId, drawable)
        binding.progressBar.isVisible = false
    }

    private fun stateHistory(tracks: List<Track>){
        if(tracks.isEmpty()){
            binding.layoutHistory.isVisible = false
        }
        else{
            historyAdaptor.data = tracks.toMutableList()
            historyAdaptor.notifyDataSetChanged()
        }
    }

    private fun responseFailShow(stringId: Int, @DrawableRes resourceId: Int){
        binding.txtViewSearchFailMessage.text = getString(stringId)
        Glide.with(requireContext())
            .load(resourceId)
            .centerCrop()
            .into(binding.imgSearchFail)

        binding.btnSearchFailUpdate.isVisible = (stringId == R.string.no_internet)

        binding.rvTrack.isVisible = false
        binding.layoutFail.isVisible = true
    }

    private fun showAudioPlayer(track: Track){
        findNavController()
            .navigate(R.id.action_searchFragment_to_audioPlayerFragment, AudioPlayerFragment.createArgs(track))
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        textWatcher?.let { binding.editTextSearch.removeTextChangedListener(it) }
        _binding = null;
    }

    companion object{
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
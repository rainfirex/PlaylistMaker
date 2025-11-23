package com.practicum.playlistmaker.ui.search.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.player.activity.AudioPlayerActivity
import com.practicum.playlistmaker.ui.search.TrackAdaptor
import com.practicum.playlistmaker.ui.search.models.SearchState
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.utils.Helper
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
//    private lateinit var viewModel: SearchViewModel

    private val viewModel: SearchViewModel by viewModel()

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllow: Boolean = true
    private var textSearch: String = ""

    private var textWatcher: TextWatcher? = null

    private val searchAdaptor = TrackAdaptor(onItemClick = { position, track ->
        if (clickItemDebounce()) {
            viewModel.addHistory(track, position)
            showAudioPlayer(track)
        }
    })

    private val historyAdaptor = TrackAdaptor(onItemClick = { position, track ->
        if (clickItemDebounce()) {
            showAudioPlayer(track)
        }
    });

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

//        viewModel = ViewModelProvider(this, SearchViewModel.getFactory()).get(SearchViewModel::class.java)
        viewModel.observeState().observe(this){
            renderSearchState(it)
        }

        binding.btnBack.setNavigationOnClickListener { finish() }

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

        binding.rvTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvTrack.adapter = searchAdaptor

        binding.rvHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvHistory.adapter = historyAdaptor
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (textSearch.isNotEmpty()){
            outState.putString(TEXT_SEARCH_KEY, textSearch)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val value = savedInstanceState.getString(TEXT_SEARCH_KEY)
        if (!value.isNullOrEmpty()){
            binding.editTextSearch.setText(value)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveHistory()
    }

    private fun responseFailShow(stringId: Int, @DrawableRes resourceId: Int){
        binding.txtViewSearchFailMessage.text = getString(stringId)
        Glide.with(applicationContext)
            .load(resourceId)
            .centerCrop()
            .into(binding.imgSearchFail)

        binding.btnSearchFailUpdate.isVisible = (stringId == R.string.no_internet)

        binding.rvTrack.isVisible = false
        binding.layoutFail.isVisible = true
    }

    private fun showAudioPlayer(track: Track){
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(AudioPlayerActivity.Companion.TRACK_KEY, track)
        startActivity(intent)
    }

    private fun clickItemDebounce(): Boolean{
        val currentState = isClickAllow
        if(isClickAllow){
            isClickAllow = false
            handler.postDelayed({ isClickAllow = true }, CLICK_DEBOUNCE_DELAY)
        }
        return currentState
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.editTextSearch.removeTextChangedListener(it) }
    }

    companion object{
        const val TEXT_SEARCH_KEY = "SEARCH_KEY"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
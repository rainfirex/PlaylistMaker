package com.practicum.playlistmaker.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.api.TrackSearchApi
import com.practicum.playlistmaker.models.Track
import com.practicum.playlistmaker.responses.TrackSearchResponse
import com.practicum.playlistmaker.services.SearchHistoryService
import com.practicum.playlistmaker.utils.Helper
import com.practicum.playlistmaker.viewholders.SearchTrackAdaptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity: AppCompatActivity() {
    private lateinit var editTxtSearch: EditText
    private var textSearch: String = ""
    private lateinit var rvTrack: RecyclerView
    private lateinit var rvHistory: RecyclerView
    private lateinit var imgSearchFail: ImageView
    private lateinit var txtViewSearchFailMessage: TextView
    private lateinit var btnSearchFailUpdate: Button
    private lateinit var layoutFail: LinearLayout
    private lateinit var layoutHistory: LinearLayout
    private lateinit var progressBar: ProgressBar

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceSearchApi = retrofit.create(TrackSearchApi::class.java)
    private lateinit var historyService: SearchHistoryService

    private val tracks = mutableListOf<Track>()

    private val searchAdaptor = SearchTrackAdaptor(onItemClick = { position, track ->
        if(clickItemDebounce()){
            historyService.add(track, position)
            showAudioPlayer(track)
        }
    })

    private val handler = Handler(Looper.getMainLooper())

    private var searchTracksRunnable = Runnable { searchTracks() }

    private var isClickAllow: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.MainLayout)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        historyService = SearchHistoryService(applicationContext, onItemClick = { _, track ->
            if(clickItemDebounce()){
                showAudioPlayer(track)
            }
        })

        searchAdaptor.data = tracks

        layoutFail = findViewById(R.id.layoutFail)
        layoutHistory = findViewById(R.id.layoutHistory)
        btnSearchFailUpdate = findViewById(R.id.btnSearchFailUpdate)
        txtViewSearchFailMessage = findViewById(R.id.txtViewSearchFailMessage)
        imgSearchFail = findViewById(R.id.imgSearchFail)
        editTxtSearch = findViewById(R.id.EditTextSearch)
        rvTrack = findViewById(R.id.rvTrack)
        rvHistory = findViewById(R.id.rvHistory)
        progressBar = findViewById(R.id.progressBar)

        val btnBack = findViewById<Toolbar>(R.id.toolbar)
        val btnClearSearch = findViewById<ImageView>(R.id.ButtonClearSearch)
        val btnClearHistory = findViewById<Button>(R.id.btnClearHistory)

        btnBack.setNavigationOnClickListener { finish() }

        btnClearSearch.setOnClickListener{
            Helper.visibleKeyboard(btnClearSearch, false)
            editTxtSearch.text?.clear()
            tracks.clear()
            searchAdaptor.notifyDataSetChanged()

            layoutFail.isVisible = false
            rvTrack.isVisible = false

            if(historyService.countItems() > 0){
                layoutHistory.isVisible = true
            }
        }

        btnClearHistory.setOnClickListener{
            historyService.clear()
            layoutHistory.isVisible = false
        }

        btnSearchFailUpdate.setOnClickListener{
            searchTracks()
        }

        editTxtSearch.doOnTextChanged{ p0: CharSequence?, p1: Int, p2: Int, p3: Int ->
            btnClearSearch.isVisible = !p0.isNullOrEmpty()

            layoutHistory.isVisible = p0.isNullOrEmpty()

            textSearch = p0.toString().trim()

            searchTracksDebounce()
        }
        editTxtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTracks()
            }
            false
        }
        editTxtSearch.setOnFocusChangeListener{ view, hasFocus ->
            layoutHistory.isVisible = (hasFocus && (view as EditText).text.isNullOrEmpty())
        }
        editTxtSearch.requestFocus()
        editTxtSearch.postDelayed({
            Helper.visibleKeyboard(editTxtSearch, true)
        }, 100)

        rvTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTrack.adapter = searchAdaptor

        rvHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvHistory.adapter = historyService.getAdaptor()

        if(historyService.countItems() == 0){
            layoutHistory.isVisible = false
        }
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
            editTxtSearch.setText(value)
        }
    }

    override fun onPause() {
        super.onPause()
        historyService.save()
    }

    private fun searchTracks(){
        if(textSearch.isEmpty()) return

        rvTrack.isVisible = false
        progressBar.isVisible = true

        serviceSearchApi.getTracks(textSearch)
            .enqueue(object : Callback<TrackSearchResponse>{

                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<TrackSearchResponse>, response: Response<TrackSearchResponse>) {
                    tracks.clear()

                    if(response.isSuccessful){
                        val data = response.body()?.results
                        if(data?.isNotEmpty() == true){
                            tracks.addAll(data)
                            rvTrack.isVisible = true
                        }
                        else{
                            responseFailShow(R.string.nothing_found, R.drawable.ic_nothing_found_120)
                        }
                    }
                    else{
                        responseFailShow(R.string.no_internet, R.drawable.ic_no_internet_120)
                    }
                    progressBar.isVisible = false
                    rvTrack.isVisible = true
                    searchAdaptor.notifyDataSetChanged()
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                    tracks.clear()
                    progressBar.isVisible = false
                    searchAdaptor.notifyDataSetChanged()
                    responseFailShow(R.string.no_internet, R.drawable.ic_no_internet_120)
                }
            })
    }

    private fun responseFailShow(stringId: Int, @DrawableRes resourceId: Int){
        txtViewSearchFailMessage.text = getString(stringId)
        Glide.with(applicationContext)
            .load(resourceId)
            .centerCrop()
            .into(imgSearchFail)

        btnSearchFailUpdate.isVisible = (stringId == R.string.no_internet)

        rvTrack.isVisible = false
        layoutFail.isVisible = true
    }

    private fun showAudioPlayer(track: Track){
        //val gson = Gson()

        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(AudioPlayerActivity.TRACK_KEY, track)
            //intent.putExtra(AudioPlayerActivity.TRACK_KEY, gson.toJson(track))
        startActivity(intent)
    }

    private fun searchTracksDebounce(){
        handler.removeCallbacks(searchTracksRunnable)
        handler.postDelayed(searchTracksRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickItemDebounce(): Boolean{
        val currentState = isClickAllow
        if(isClickAllow){
            isClickAllow = false
            handler.postDelayed({ isClickAllow = true }, CLICK_DEBOUNCE_DELAY)
        }
        return currentState
    }

    companion object{
        private const val BASE_URL = "https://itunes.apple.com"
        const val TEXT_SEARCH_KEY = "SEARCH_KEY"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
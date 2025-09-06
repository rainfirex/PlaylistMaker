package com.practicum.playlistmaker.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
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
    private var editTxtSearch: EditText? = null
    private var textSearch: String? = null
    private lateinit var rvTrack: RecyclerView
    private lateinit var rvHistory: RecyclerView
    private lateinit var imgSearchFail: ImageView
    private lateinit var txtViewSearchFailMessage: TextView
    private lateinit var btnSearchFailUpdate: Button
    private lateinit var layoutFail: LinearLayout
    private lateinit var layoutHistory: LinearLayout

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceSearchApi = retrofit.create(TrackSearchApi::class.java)
    private lateinit var historyService: SearchHistoryService

    private val tracks = mutableListOf<Track>()

    private val searchAdaptor = SearchTrackAdaptor(onItemClick = { position, track ->
        historyService.add(track, position)
        Toast.makeText(this, track.trackName, Toast.LENGTH_SHORT).show()
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.MainLayout)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        historyService = SearchHistoryService(this)

        searchAdaptor.data = tracks

        layoutFail = findViewById(R.id.layoutFail)
        layoutHistory = findViewById(R.id.layoutHistory)
        btnSearchFailUpdate = findViewById(R.id.btnSearchFailUpdate)
        txtViewSearchFailMessage = findViewById(R.id.txtViewSearchFailMessage)
        imgSearchFail = findViewById(R.id.imgSearchFail)
        editTxtSearch = findViewById(R.id.EditTextSearch)
        rvTrack = findViewById(R.id.rvTrack)
        rvHistory = findViewById(R.id.rvHistory)

        val btnBack = findViewById<Toolbar>(R.id.toolbar)
        val btnClearSearch = findViewById<ImageView>(R.id.ButtonClearSearch)
        val btnClearHistory = findViewById<Button>(R.id.btnClearHistory)

        btnBack.setNavigationOnClickListener { finish() }

        btnClearSearch.setOnClickListener{
            Helper.visibleKeyboard(btnClearSearch, false)
            editTxtSearch?.text?.clear()
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

        val searchWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                btnClearSearch.isVisible = !p0.isNullOrEmpty()

                layoutHistory.isVisible = p0.isNullOrEmpty()

                textSearch = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }

        btnSearchFailUpdate.setOnClickListener{
            if (textSearch != null)
                searchTracks(textSearch!!)
        }

        editTxtSearch?.addTextChangedListener(searchWatcher)
        editTxtSearch?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val txt = editTxtSearch!!.text.trim().toString()
                searchTracks(txt)
            }
            false
        }
        editTxtSearch?.setOnFocusChangeListener{ view, hasFocus ->
            layoutHistory.isVisible = (hasFocus && (view as EditText).text.isNullOrEmpty())
        }
        editTxtSearch?.requestFocus()
        editTxtSearch?.postDelayed({
            Helper.visibleKeyboard(editTxtSearch as View, true)
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

        if (!textSearch.isNullOrEmpty()){
            outState.putString(TEXT_SEARCH_KEY, textSearch)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val value = savedInstanceState.getString(TEXT_SEARCH_KEY)
        if (!value.isNullOrEmpty()){
            editTxtSearch?.setText(value)
        }
    }

    override fun onPause() {
        super.onPause()
        historyService.save()
    }

    private fun searchTracks(text: String){
        if(text.isEmpty()) return

        serviceSearchApi.getTracks(text)
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
                    searchAdaptor.notifyDataSetChanged()
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                    tracks.clear()
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

    companion object{
        private const val BASE_URL = "https://itunes.apple.com"
        const val TEXT_SEARCH_KEY = "SEARCH_KEY"
    }
}
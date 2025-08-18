package com.practicum.playlistmaker.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
import com.practicum.playlistmaker.viewholders.SearchTrackAdaptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity: AppCompatActivity() {
    companion object{
        private const val BASE_URL = "https://itunes.apple.com"
        const val TEXT_SEARCH_KEY = "SEARCH_KEY"
    }

    private var editTxtSearch: EditText? = null
    private var textSearch: String? = null
    private lateinit var rvTrack: RecyclerView
    private lateinit var imgSearchFail: ImageView
    private lateinit var txtViewSearchFailMessage: TextView
    private lateinit var btnSearchFailUpdate: Button
    private lateinit var layoutFail: LinearLayout

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceSearch = retrofit.create(TrackSearchApi::class.java)

    private val tracks = ArrayList<Track>()

    private val adaptor = SearchTrackAdaptor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.MainLayout)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        adaptor.data = tracks

        layoutFail = findViewById(R.id.layoutFail)
        btnSearchFailUpdate = findViewById(R.id.btnSearchFailUpdate)
        txtViewSearchFailMessage = findViewById(R.id.txtViewSearchFailMessage)
        imgSearchFail = findViewById(R.id.imgSearchFail)
        editTxtSearch = findViewById(R.id.EditTextSearch)
        val btnBack = findViewById<Toolbar>(R.id.toolbar)
        val btnClearSearch = findViewById<ImageView>(R.id.ButtonClearSearch)

        btnBack.setNavigationOnClickListener { finish() }

        btnClearSearch.setOnClickListener{
            editTxtSearch?.text?.clear()

            tracks.clear()
            adaptor.notifyDataSetChanged()

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(btnClearSearch.windowToken, 0)
        }

        val searchWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                btnClearSearch.isVisible = ! p0.isNullOrEmpty()
                textSearch = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
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
                true
            }
            false
        }

        rvTrack = findViewById(R.id.rvTrack)
        rvTrack.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvTrack.adapter = adaptor
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

    private fun searchTracks(text: String){
        serviceSearch.getTracks(text)
            .enqueue(object : Callback<TrackSearchResponse>{

                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<TrackSearchResponse>, response: Response<TrackSearchResponse>) {
                    tracks.clear()

                    if(response.code() == 200){
                        if(response.body()?.results?.isNotEmpty() == true){
                            tracks.addAll(response.body()?.results!!)
                            rvTrack.isVisible = true
                        }
                        else{
                            responseFailShow(R.string.nothing_found, R.drawable.ic_nothing_found_120)
                        }
                    }
                    else{
                        responseFailShow(R.string.no_internet, R.drawable.ic_no_internet_120)
                    }
                    adaptor.notifyDataSetChanged()
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                    tracks.clear()
                    adaptor.notifyDataSetChanged()
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
}
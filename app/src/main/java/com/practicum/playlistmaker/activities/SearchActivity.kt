package com.practicum.playlistmaker.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R

class SearchActivity: AppCompatActivity() {

    private var editTxtSearch: EditText? = null
    private var textSearch: String? = null

    companion object{
        const val TEXT_SEARCH_KEY = "SEARCH_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        editTxtSearch = findViewById(R.id.EditTextSearch)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnClearSearch = findViewById<ImageView>(R.id.ButtonClearSearch)

        btnBack.setOnClickListener{ finish() }

        btnClearSearch.setOnClickListener{ editTxtSearch?.text?.clear() }

        val searchWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()){
                    btnClearSearch.visibility = View.VISIBLE
                    textSearch = p0.toString()
                }
                else{
                    btnClearSearch.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        editTxtSearch?.addTextChangedListener(searchWatcher)
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
}

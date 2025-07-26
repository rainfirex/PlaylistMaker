package com.practicum.playlistmaker.activities

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.practicum.playlistmaker.R

class SearchActivity: AppCompatActivity() {

    private var editTxtSearch: EditText? = null
    private var textSearch: String? = null

    companion object{
        const val TEXT_SEARCH_KEY = "SEARCH_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.MainLayout)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }

        editTxtSearch = findViewById(R.id.EditTextSearch)
        val btnBack = findViewById<Toolbar>(R.id.toolbar)
        val btnClearSearch = findViewById<ImageView>(R.id.ButtonClearSearch)

        btnBack.setNavigationOnClickListener { finish() }

        btnClearSearch.setOnClickListener{
            editTxtSearch?.text?.clear()

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

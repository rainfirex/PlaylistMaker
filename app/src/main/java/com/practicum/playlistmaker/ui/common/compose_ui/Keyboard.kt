package com.practicum.playlistmaker.ui.common.compose_ui

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView

@Composable
fun keyboardVisibility(): (Boolean) -> Unit {
    val view = LocalView.current

    return { isShow: Boolean ->
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (isShow) {
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        } else {
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
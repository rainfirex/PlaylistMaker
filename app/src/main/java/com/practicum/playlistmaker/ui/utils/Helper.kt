package com.practicum.playlistmaker.ui.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager

class Helper {
    companion object{
        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics).toInt()
        }

        fun visibleKeyboard(v: View, isShow: Boolean){
            val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                ?: return

            if(isShow)
            {
                imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
            }
            else{
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }
}
package com.practicum.playlistmaker.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.File
import java.io.FileOutputStream

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

        fun getFileNameFromUri(context: Context, uri: Uri): String? {
            var fileName: String? = null
            val cursor = context.contentResolver.query(uri, null, null, null, null)

            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                    if (nameIndex >= 0) {
                        fileName = it.getString(nameIndex)
                    }
                }
            }
            return fileName
        }

        fun saveImage(context: Context, uri: Uri, albumName: String, filename: String): String{
            val path = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName)

            if (!path.exists()){
                path.mkdirs()
            }

            val file = File(path, filename)

            val uriStream = context.contentResolver.openInputStream(uri)

            val fileStream = FileOutputStream(file)

            BitmapFactory
                .decodeStream(uriStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, fileStream)

            return file.path
        }
    }
}
package com.practicum.playlistmaker.ui.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.practicum.playlistmaker.R

class ChangeInternetBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != ACTION && context != null) { return }
        if(!isConnected(context)){
            val message = context?.getString(R.string.no_connect_internet)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isConnected(context: Context?): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    private companion object{
        const val ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
    }
}
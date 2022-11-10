package com.byandev.storyapp.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class UtilsConnect (private val context: Context) {
    fun isConnectedToInternet(): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        for (i in info.indices)
            if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        return false
    }
}
package com.wl.radio.util

import android.content.Context
import android.net.ConnectivityManager

object  NetworkUtil {

    fun isNetworkConnected(context: Context): Boolean? {
        var context = context
        try {
            context = context.applicationContext
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm != null) {
                val networkInfo = cm.activeNetworkInfo
                return networkInfo != null && networkInfo.isConnected
            }
        } catch (ignored: Exception) {
        }

        return null
    }
}
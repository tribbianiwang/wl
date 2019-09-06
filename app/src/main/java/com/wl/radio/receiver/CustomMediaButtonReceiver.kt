package com.wl.radio.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class CustomMediaButtonReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("CustomMediaButton","onreceive")
    }
}
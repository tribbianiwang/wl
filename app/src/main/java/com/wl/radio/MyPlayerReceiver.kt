package com.wl.radio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager

class MyPlayerReceiver :BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        println("MyPlayerReceiver.onReceive $intent")
        if (intent?.getAction() == "com.app.test.android.Action_Close") {
            XmPlayerManager.release()
        } else if (intent?.getAction() == "com.app.test.android.Action_PAUSE_START") {
            if (XmPlayerManager.getInstance(context).isPlaying) {
                XmPlayerManager.getInstance(context).pause()
            } else {
                XmPlayerManager.getInstance(context).play()
            }
        }
    }
}
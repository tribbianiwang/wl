package com.wl.radio.receiver

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.wl.radio.util.ActivityUtil
import com.wl.radio.util.Constants.CLOSE_APP_ACTION
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager

class MyPlayerReceiver :BroadcastReceiver(){
    val TAG = "MyPlayerReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG,"onReceive"+intent?.action)
        println("MyPlayerReceiver.onReceive $intent")
        if (intent?.getAction() == "com.app.test.android.Action_Close") {
//            context?.sendBroadcast(Intent(CLOSE_APP_ACTION))

            val mActivityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val mList = mActivityManager.runningAppProcesses
            for (runningAppProcessInfo in mList) {
                if (runningAppProcessInfo.pid != android.os.Process.myPid()) {
                    android.os.Process.killProcess(runningAppProcessInfo.pid)
                }
            }
            android.os.Process.killProcess(android.os.Process.myPid())


        } else if (intent?.getAction() == "com.app.test.android.Action_PAUSE_START") {
            if (XmPlayerManager.getInstance(context).isPlaying) {
                XmPlayerManager.getInstance(context).pause()
            } else {
                XmPlayerManager.getInstance(context).play()
            }
        }
    }
}
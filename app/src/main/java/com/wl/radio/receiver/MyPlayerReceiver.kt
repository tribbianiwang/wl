package com.wl.radio.receiver

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.wl.radio.MyApplication
import com.wl.radio.util.ActivityUtil
import com.wl.radio.util.Constants
import com.wl.radio.util.Constants.CLOSE_APP_ACTION
import com.wl.radio.util.Constants.NEXT_SHOW_ACTION
import com.wl.radio.util.Constants.PRE_SHOW_ACTION
import com.wl.radio.util.Constants.START_OR_PAUSE_ACTION
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager

class MyPlayerReceiver :BroadcastReceiver(){
    val TAG = "MyPlayerReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(TAG,"action:"+intent?.action+"listsize:"+XmPlayerManager.getInstance(context).playList.size)
        if (intent?.getAction() ==CLOSE_APP_ACTION) {


            context?.let { ActivityUtil.exitActivity(it) }


        } else if (intent?.getAction() == START_OR_PAUSE_ACTION) {
            if (XmPlayerManager.getInstance(context).isPlaying) {
                XmPlayerManager.getInstance(context).pause()
            } else {
                XmPlayerManager.getInstance(context).play()
            }
        }else if(intent?.action== PRE_SHOW_ACTION){

            context?.sendBroadcast(Intent(Constants.APPLICATION_PRE_SHOW_ACTION))

//            MyApplication.playPreRadio()


        }else if(intent?.action== NEXT_SHOW_ACTION){
            context?.sendBroadcast(Intent(Constants.APPLICATION_NEXT_SHOW_ACTION))
//            MyApplication.playNextRadio()
        }
    }
}
package com.wl.radio

import android.app.Application
import com.billy.android.loading.Gloading
import com.wl.radio.adapter.GlobalAdapter
import com.wl.radio.util.Constants.XMLYAPPSECRET
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import android.app.PendingIntent
import android.content.Intent
import com.wl.radio.receiver.MyPlayerReceiver
import com.wl.radio.util.Constants.CLOSE_APP_ACTION
import com.wl.radio.util.Constants.START_OR_PAUSE_ACTION
import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater
import com.ximalaya.ting.android.opensdk.util.BaseUtil



class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Gloading.debug(BuildConfig.DEBUG)
        Gloading.initDefault(GlobalAdapter())
        CommonRequest.getInstanse().init(this, XMLYAPPSECRET);
        XmPlayerManager.getInstance(this).init()


        if (BaseUtil.getCurProcessName(this).contains(":player")) {
            val instanse = XmNotificationCreater.getInstanse(this)
            instanse.setNextPendingIntent(null as PendingIntent?)
            instanse.setPrePendingIntent(null as PendingIntent?)
            instanse.setStartOrPausePendingIntent(null as PendingIntent?)




            val closeAppIntent = Intent(CLOSE_APP_ACTION)
            closeAppIntent.setClass(this, MyPlayerReceiver::class.java!!)
            val closeAppBroadcast = PendingIntent.getBroadcast(this, 0, closeAppIntent, 0)
            instanse.setClosePendingIntent(closeAppBroadcast)


            val startOrPauseIntent = Intent(START_OR_PAUSE_ACTION)
            startOrPauseIntent.setClass(this, MyPlayerReceiver::class.java!!)
            val startOrPauseBroadcast = PendingIntent.getBroadcast(this,0,startOrPauseIntent,0)
            instanse.setStartOrPausePendingIntent(startOrPauseBroadcast)





        }
    }


}
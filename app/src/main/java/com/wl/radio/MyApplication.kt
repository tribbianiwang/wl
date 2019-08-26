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

            val actionName = "com.app.test.android.Action_Close"
            val intent = Intent(actionName)
            intent.setClass(this, MyPlayerReceiver::class.java!!)
            val broadcast = PendingIntent.getBroadcast(this, 0, intent, 0)
            instanse.setClosePendingIntent(broadcast)
        }
    }


}
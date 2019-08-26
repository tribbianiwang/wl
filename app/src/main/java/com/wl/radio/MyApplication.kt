package com.wl.radio

import android.app.Application
import com.billy.android.loading.Gloading
import com.wl.radio.adapter.GlobalAdapter
import com.wl.radio.util.Constants.XMLYAPPSECRET
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.wl.radio.receiver.MyPlayerReceiver
import com.wl.radio.util.Constants.CLOSE_APP_ACTION
import com.wl.radio.util.Constants.NEXT_SHOW_ACTION
import com.wl.radio.util.Constants.PRE_SHOW_ACTION
import com.wl.radio.util.Constants.START_OR_PAUSE_ACTION
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater
import com.ximalaya.ting.android.opensdk.util.BaseUtil


class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        Gloading.debug(BuildConfig.DEBUG)
        Gloading.initDefault(GlobalAdapter())
        CommonRequest.getInstanse().init(this, XMLYAPPSECRET);
        XmPlayerManager.getInstance(this).init()
        context = this




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
            val startOrPauseBroadcast = PendingIntent.getBroadcast(this, 0, startOrPauseIntent, 0)
            instanse.setStartOrPausePendingIntent(startOrPauseBroadcast)

            val nextShowIntent = Intent(NEXT_SHOW_ACTION)
            nextShowIntent.setClass(this, MyPlayerReceiver::class.java!!)
            val nextShowBroadcast = PendingIntent.getBroadcast(this, 0, nextShowIntent, 0)
            instanse.setNextPendingIntent(nextShowBroadcast)


            val preShowIntent = Intent(PRE_SHOW_ACTION)
            preShowIntent.setClass(this, MyPlayerReceiver::class.java!!)
            val preShowBroadcast = PendingIntent.getBroadcast(this, 0, preShowIntent, 0)
            instanse.setPrePendingIntent(preShowBroadcast)
        }
    }

    companion object {
        var radioList: ArrayList<Radio> = ArrayList()
        var  context:Application? = null
        val TAG = "MyApplication"
        fun getContext():Context{
            return context!!
        }
        fun playNextRadio(): Radio? {
            Log.d(TAG,"playNextRadio"+getPlayingRadioIndex())
            if (radioList.size > 0 && getPlayingRadioIndex() < (radioList.size - 1)) {
                XmPlayerManager.getInstance(getContext()).playActivityRadio(radioList[getPlayingRadioIndex() + 1])
                return radioList[getPlayingRadioIndex() + 1]
            }else{
                return null
            }

        }


        fun playPreRadio():Radio?{
            Log.d(TAG,"playPreRadio"+getPlayingRadioIndex())
            if (radioList.size > 0 && getPlayingRadioIndex() != 0) {
                XmPlayerManager.getInstance(getContext()).playActivityRadio(radioList[getPlayingRadioIndex() - 1])
                return radioList[getPlayingRadioIndex()-1]
            }else{
                return null
            }

        }


        fun getPlayingRadioIndex(): Int {
            for (radioIndex in radioList.indices) {
                if (radioList[radioIndex].dataId.equals(XmPlayerManager.getInstance(getContext()).currSound.dataId)) {
                    return radioIndex
                } else if (radioIndex == (radioList.size - 1)) {
                    break;
                }
            }
            return 0
        }

        fun refreshRadioList(newRadioList:ArrayList<Radio>){

            if(radioList.size>0){
                radioList.clear()
            }

            radioList.addAll(newRadioList)
        }

    }
}
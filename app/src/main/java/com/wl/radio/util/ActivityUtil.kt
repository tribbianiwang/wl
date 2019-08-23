package com.wl.radio.util

import android.app.Activity
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager

class ActivityUtil {
    object  list{
        var list:ArrayList<Activity>?=null
    }

    companion object {
        fun addActivity(activity:Activity){
            list.list?.add(activity)
        }


        fun removeActivity(activity: Activity){
            list.list?.remove(activity)
        }

        fun exitActivity(){
            XmPlayerManager.release()
            list.list?.clear();
            System.exit(0);

        }

    }

}
package com.wl.radio.util

import android.app.Activity
import android.util.Log
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager

object ActivityUtil {
     val TAG = "ActivityUtil"

        var activityList:ArrayList<Activity>?= ArrayList()


        fun addActivity(activity:Activity){
            Log.d(TAG,"addActivity:activityname"+activity.localClassName)
            activityList?.add(activity)
            Log.d(TAG,"addActivity:size"+activityList?.size)
        }


        fun removeActivity(activity: Activity){
            Log.d(TAG,"removeActivity"+activity.localClassName)
            activityList?.remove(activity)
        }

        fun exitActivity(){


            Log.d(TAG,"activityListSize"+ activityList?.size)
            if(activityList!=null){
                for(activity in activityList!!){
                    activity.finish()

                }
            }
            activityList?.clear()
            XmPlayerManager.release()
            System.exit(0)

        }



}
package com.wl.radio.util

import android.content.Context
import com.wl.radio.MyApplication
import java.util.*

object  StringUtils{
    fun getColor(colorId: Int): Int {
        return MyApplication.getContext().getResources().getColor(colorId)
    }

    fun getString(stringId: Int): String {
        return MyApplication.getContext().getResources().getString(stringId)
    }

    fun getRString(context: Context, id: Int): String {
        return context.resources.getString(id)
    }

    fun getRColor(context: Context?, id: Int): Int {
        return context?.resources?.getColor(id)!!
    }

    fun formatPlayCount(playCount: Long): String {
        var standardPlayCount = ""
        if (playCount < 0) {
            standardPlayCount = "收听人数:0"
        } else if (playCount < 10000) {
            standardPlayCount = "收听人数:"+playCount.toString()
        } else if (playCount < 100000000) {
            standardPlayCount =
                String.format(Locale.getDefault(), "收听人数:%d.%02d万", playCount / 10000, playCount % 10000 / 100)
        } else if (playCount > 100000000) {
            standardPlayCount =
                String.format(Locale.getDefault(), "收听人数:%d.%02d亿", playCount / 100000000, playCount % 100000000 / 1000000)
        }
        return standardPlayCount
    }

}
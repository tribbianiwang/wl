package com.wl.radio.util

import android.content.Context
import com.wl.radio.MyApplication
import java.util.*
import java.text.ParseException
import java.text.SimpleDateFormat




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
            standardPlayCount = "0"
        } else if (playCount < 10000) {
            standardPlayCount = playCount.toString()
        } else if (playCount < 100000000) {
            standardPlayCount =
                String.format(Locale.getDefault(), "%d.%02d万", playCount / 10000, playCount % 10000 / 100)
        } else if (playCount > 100000000) {
            standardPlayCount =
                String.format(Locale.getDefault(), "收听人数:%d.%02d亿", playCount / 100000000, playCount % 100000000 / 1000000)
        }
        return standardPlayCount
    }


    fun getWeekOfDate(date: Date?): String {
        val weekOfDays = arrayOf("0", "1", "2", "3", "4", "5", "6")
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }
        var w = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (w < 0) {
            w = 0
        }
        return weekOfDays[w]
    }


    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    fun getOldDate(distanceDay: Int): Date? {
        val dft = SimpleDateFormat("yyyy-MM-dd")
        val beginDate = Date()
        val date = Calendar.getInstance()
        date.time = beginDate
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay)
        var endDate: Date? = null
        try {
            endDate = dft.parse(dft.format(date.time))
        } catch (e: ParseException) {
            e.printStackTrace()
        }



        return endDate
    }


    fun isEffectiveDate(nowTime: Date, startTime: Date, endTime: Date): Boolean {
        if (nowTime.time === startTime.time || nowTime.time === endTime.time) {
            return true
        }

        val date = Calendar.getInstance()
        date.time = nowTime

        val begin = Calendar.getInstance()
        begin.time = startTime

        val end = Calendar.getInstance()
        end.time = endTime

        return if (date.after(begin) && date.before(end)) {
            true
        } else {
            false
        }
    }



}
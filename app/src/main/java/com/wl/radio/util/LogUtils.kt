package com.wl.radio.util

import android.util.Log

object  LogUtils {

    private val DEBUG = true
    fun v(tag: String, msg: String) {
        logger("v", tag, msg)
    }

    fun d(tag: String, msg: String) {
        logger("d", tag, msg)
    }

    fun i(tag: String, msg: String) {
        logger("i", tag, msg)
    }

    fun w(tag: String, msg: String) {
        logger("w", tag, msg)
    }

    fun e(tag: String, msg: String) {
        logger("e", tag, msg)
    }

    private fun logger(priority: String, tag: String, msg: String) {
        if (!DEBUG) {
            return
        }
        when (priority) {
            "v" -> Log.v(tag, msg)
            "d" -> Log.d(tag, msg)
            "i" -> Log.i(tag, msg)
            "w" -> Log.w(tag, msg)
            else -> Log.e(tag, msg)
        }
    }

}
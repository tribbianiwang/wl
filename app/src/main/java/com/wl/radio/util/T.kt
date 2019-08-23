package com.wl.radio.util

import android.content.Context
import android.widget.Toast

object T {
    private var toast: Toast? = null
    var isShow = true
    private var lastClickTime: Long = 0
    fun showShort(context: Context, message: CharSequence) {
        val time = System.currentTimeMillis()
        if (time - lastClickTime < 2 * Constants.TOAST_DOUBLE_TIME_LIMIT) {
            return
        }
        lastClickTime = time

        if (isShow)
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        //		toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast?.show()
    }


}
package com.wl.radio.util

import android.view.View

interface RvItemLongClickListener {
    abstract fun onItemLongClick(view: View, position: Int)
}
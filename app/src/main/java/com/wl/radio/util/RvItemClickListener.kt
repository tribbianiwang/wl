package com.wl.radio.util

import android.view.View

interface RvItemClickListener {
    abstract fun onItemClick(view: View, position: Int)
}
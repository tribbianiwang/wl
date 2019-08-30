package com.wl.radio.dialog

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.wl.radio.R

class ProgressDialog(context: Context,dialogStyle:Int):AlertDialog(context,dialogStyle) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_dialog_progress)

    }
}
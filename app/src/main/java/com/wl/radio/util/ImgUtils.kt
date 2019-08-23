package com.wl.radio.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import com.bumptech.glide.request.RequestOptions
import com.wl.radio.R


object ImgUtils {
    fun showImage(context: Context, imageUrl: String, imageView: ImageView) {

        val options = RequestOptions()
        options.centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.icon_placeholder_gray).error(R.drawable.icon_placeholder_gray)
        Glide.with(context)
            .load(imageUrl)
            .apply(options)
            .into(imageView)


    }
}
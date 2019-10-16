package com.wl.radio.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.wl.radio.R
import android.graphics.Bitmap
import androidx.core.graphics.drawable.DrawableCompat
import com.wl.radio.view.GramophoneView
import jp.wasabeef.blurry.Blurry
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


object ImgUtils {
    fun showImage(context: Context, imageUrl: String, imageView: ImageView) {

        val options = RequestOptions()
        options.centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.icon_placeholder_gray).error(R.drawable.icon_placeholder_gray)
        Glide.with(context)
            .load(imageUrl)
            .apply(options)
            .into(imageView)
    }

    fun showColorIcon(imageView:ImageView){
        imageView.setColorFilter(StringUtils.getColor(R.color.colorPrimary));

    }

    fun showColorIcon(imageView:ImageView,colorId:Int){
        imageView.setColorFilter(StringUtils.getColor(colorId));

    }



    //    fun getImgeUrlBitmap(context: Context, imageUrl: String):Bitmap{
    fun showImgeUrlBitmap(
        context: Context,
        imageUrl: String,
        givCover: GramophoneView
    ){
//        Observable<Bitmap>.addObserver()
        Observable.create<Bitmap> {
            it.onNext(
                Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .submit().get()
            )
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<Bitmap>() {
                override fun onNext(bitmap: Bitmap) {

                    givCover.setPictureBitmMap(bitmap)
                }

                override fun onCompleted() {


                }

                override fun onError(e: Throwable?) {


                }

            })



    }


    fun showImgeUrlBitmapBlur(
        context: Context,
        imageUrl: String,
        ivlogo: ImageView
    ){
//        Observable<Bitmap>.addObserver()
        Observable.create<Bitmap> {
            it.onNext(
                Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .submit().get()
            )
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<Bitmap>() {
                override fun onNext(bitmap: Bitmap) {

                    // from Bitmap
                    Blurry.with(context).from(bitmap).into(ivlogo)
                }

                override fun onCompleted() {


                }

                override fun onError(e: Throwable?) {


                }

            })



    }
}
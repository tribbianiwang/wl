package com.wl.radio.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.billy.android.loading.Gloading

open class BaseFragment :Fragment(){

    private var mHolder: Gloading.Holder? = null

    private var imageView: ImageView? = null




    open fun initGloading(rootView:View){
        mHolder = Gloading.getDefault().wrap(view).withRetry {
            //change picture url to a correct one
            onLoadRetry()
        }

    }


    open fun onLoadRetry() {
        // override this method in subclass to do retry task
    }

    fun showLoading() {
       
        mHolder?.showLoading()
        Log.d("BaseFragment","loading"+mHolder)
    }

    fun showLoadSuccess() {
       
        mHolder?.showLoadSuccess()
    }

    fun showLoadFailed() {
       
        mHolder?.showLoadFailed()
    }

    fun showEmpty() {
       
        mHolder?.showEmpty()
    }



}
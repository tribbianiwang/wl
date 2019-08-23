package com.wl.radio.view

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager
import android.view.MotionEvent



class NoScrollViewPager :ViewPager {

    var isCanScroll:Boolean=false
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        // TODO Auto-generated method stub
        return if (isCanScroll) {
            super.onTouchEvent(arg0)
        } else {
            false
        }

    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        // TODO Auto-generated method stub
        return if (isCanScroll) {
            super.onInterceptTouchEvent(arg0)
        } else {
            false
        }

    }


}
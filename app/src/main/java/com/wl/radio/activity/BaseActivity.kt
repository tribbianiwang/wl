package com.wl.radio.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.billy.android.loading.Gloading
import com.wl.radio.R
import com.wl.radio.util.ActivityUtil




open class BaseActivity : AppCompatActivity(){


    protected var mHolder: Gloading.Holder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            ActivityUtil.addActivity(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        ActivityUtil.removeActivity(this)
    }


    protected fun initLoadingStatusViewIfNeed() {
        if (mHolder == null) {
            //bind status view to activity root view by default
            mHolder = Gloading.getDefault().wrap(this).withRetry { onLoadRetry() }
        }
    }


     open fun onLoadRetry() {
        // override this method in subclass to do retry task
    }

    fun showLoading() {
        initLoadingStatusViewIfNeed()
        mHolder?.showLoading()
        Log.d("BaseActivity","loading")
    }

    fun showLoadSuccess() {
        initLoadingStatusViewIfNeed()
        mHolder?.showLoadSuccess()
    }

    fun showLoadFailed() {
        initLoadingStatusViewIfNeed()
        mHolder?.showLoadFailed()
    }

    fun showEmpty() {
        initLoadingStatusViewIfNeed()
        mHolder?.showEmpty()
    }


    fun skipActivity(targetActivity: Class<*>) {
        val intent = Intent(this, targetActivity)
        startActivity(intent)
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)

        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out)
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out)
    }

    override fun startActivityForResult(
        intent: Intent,
        requestCode: Int, options: Bundle?
    ) {
        super.startActivityForResult(intent, requestCode, options)
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out)
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out)
    }


}
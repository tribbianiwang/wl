package com.wl.radio.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.billy.android.loading.Gloading
import com.wl.radio.R
import com.wl.radio.adapter.GlobalAdapter
import com.wl.radio.dialog.ProgressDialog
import com.wl.radio.util.Constants
import com.wl.radio.util.T

import com.wl.radio.util.Constants.QUERYSTATUSLOADING


open class BaseFragment : ImmersionFragment() {

    lateinit var mContext: Activity

    // 防暴力点击
    protected var lastClickTime: Long = 0

    protected var mHolder: Gloading.Holder? = null

    private var connectivityManager: ConnectivityManager? = null
    private var info: NetworkInfo? = null

    private val fragmentBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {


                ConnectivityManager.CONNECTIVITY_ACTION -> {
                    connectivityManager =
                        activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    info = connectivityManager!!.activeNetworkInfo
                    if (info != null && info!!.isAvailable) {
                        val name = info!!.typeName
                        netWorkSuccess()

                    } else {

                        netWorkFailed()
                    }
                }
            }

        }
    }
    private var hasNetWorkFailed = false


    var progressDialog: ProgressDialog? = null


    var queryStatusObserver: Observer<String> = Observer { status ->
        when (status) {
            QUERYSTATUSLOADING -> showProgress(activity)

            Constants.QUERYSTATUSSUCCESS -> hideProgress()

            Constants.QUERYSTATUSFAILED -> hideProgress()
        }
    }


    var gLoadingqueryStatusObserver: Observer<String> = Observer { status ->
        when (status) {
            Constants
                .QUERYSTATUSLOADING -> showLoading()

            Constants.QUERYSTATUSSUCCESS -> showLoadSuccess()

            Constants.QUERYSTATUSFAILED -> showLoadFailed()
            Constants.QUERYSTATUSEMPTY ->

                showEmpty()
        }
    }


    var errorMsgObserver: Observer<String> = Observer { s -> T.showShort(activity!!, s!!) }

    fun loginOutSuccess() {

    }

    fun loginSuccess() {

    }

    fun netWorkSuccess() {
        if (hasNetWorkFailed) {
            onLoadRetry()
            hasNetWorkFailed = false
        }

    }

    fun netWorkFailed() {
        hasNetWorkFailed = true

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as Activity

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        mContext.registerReceiver(fragmentBroadcastReceiver, intentFilter)

        Gloading.initDefault(GlobalAdapter())

    }

    protected fun initLoadingStatusView(rootView: View): Gloading.Holder ?{
        if (mHolder == null) {
            //bind status view to activity root view by default
            mHolder = Gloading.getDefault().wrap(rootView).withRetry { onLoadRetry() }
        }
        return mHolder
    }


    open fun onLoadRetry() {
        // override this method in subclass to do retry task
    }

    fun showLoading() {
        //        initLoadingStatusViewIfNeed();
        if (mHolder != null) {
            mHolder!!.showLoading()
        }



    }

    fun showLoadSuccess() {
        //        initLoadingStatusViewIfNeed();
        if (mHolder != null) {
            mHolder!!.showLoadSuccess()
        }

    }

    fun showLoadFailed() {
        //        initLoadingStatusViewIfNeed();
        if (mHolder != null) {
            mHolder!!.showLoadFailed()
        }

    }

    fun showEmpty() {
        if (mHolder != null) {
            mHolder!!.showEmpty()
        }
        //        initLoadingStatusViewIfNeed();

    }


    /**
     * 防暴力点击 上次点击时间, lastClickTime 本次点击时间, time 时间差, timeD 多长时间内点击无效, timelong
     */
    protected fun isFastDoubleClick(timelong: Int): Boolean {
        val time = System.currentTimeMillis()
        val timeD = time - lastClickTime
        if (timeD > timelong) {
            lastClickTime = time
            return true
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mContext.unregisterReceiver(fragmentBroadcastReceiver)


    }

    override fun initImmersionBar() {

    }

    /**
     * 显示等待对话框，一般用于网络请求的时候
     *
     * @param context
     */
    fun showProgress(context: Context?) {
        if (context != null && !(context as Activity).isFinishing) {
            if (progressDialog == null) {
                progressDialog = ProgressDialog(context, R.style.primaryDialogStyle)
            }
            progressDialog!!.show()
        }
    }


    /**
     * 关闭等待对话框
     */
    fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }

    companion object {
        private val TAG = "BaseFragment"
    }

}
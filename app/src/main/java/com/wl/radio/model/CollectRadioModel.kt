package com.wl.radio.model

import android.provider.ContactsContract
import android.provider.SyncStateContract
import android.util.Log
import com.wl.radio.MyApplication
import com.wl.radio.R
import com.wl.radio.bean.CollectRadioBean
import com.wl.radio.util.Constants
import com.wl.radio.util.StringUtils
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class CollectRadioModel(dataResultListener: DataResultListener) {

    var dataResultListener: DataResultListener

    init {
        this.dataResultListener = dataResultListener
    }

    interface DataResultListener {
        fun setQueryStatus(status: String)
        fun setErrorMsg(msg: String)
        fun setAllCollectRadio(collectRadios: List<CollectRadioBean>)
    }


    fun addCollectRadio(radioId: String) {
        dataResultListener.setQueryStatus(Constants.QUERYSTATUSLOADING)
        Observable.create<String> {
            if (MyApplication.collectRadioDao.queryCollectRadioById(radioId) == null) {
                if(radioId=="0"){
                   it.onError(Throwable(StringUtils.getString(R.string.string_collect_error)))
                }else{

                    MyApplication.collectRadioDao.insertCollectRadio(CollectRadioBean(radioId))
                    it.onNext(Constants.QUERYSTATUSSUCCESS)
                }
            } else {
                it.onError(Throwable(StringUtils.getString(R.string.string_already_collect)))
            }


        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<String>() {
                override fun onNext(t: String) {
                    dataResultListener.setQueryStatus(t)

                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                    dataResultListener.setQueryStatus(Constants.QUERYSTATUSFAILED)
                    dataResultListener.setErrorMsg(e.message.toString())
                }

            })


    }


    fun deleteCollectRadio(collectRadioBean: CollectRadioBean) {

        dataResultListener.setQueryStatus(Constants.QUERYSTATUSLOADING)
        Observable.create<String> {
            MyApplication.collectRadioDao.deleteCollectRadio(collectRadioBean)
            it.onNext(Constants.QUERYDELETESUCCESS)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<String>() {
                override fun onNext(t: String) {
                    dataResultListener.setQueryStatus(t)
                    Log.d("collectRadioModel","success:")
                }

                override fun onCompleted() {


                }

                override fun onError(e: Throwable) {
                    Log.e("collectRadioModel","error:"+e.message.toString())
                    dataResultListener.setQueryStatus(Constants.QUERYSTATUSFAILED)
                    dataResultListener.setErrorMsg(e.message.toString())

                }

            })

    }


    fun queryAllCollectRadio() {
        dataResultListener.setQueryStatus(Constants.QUERYSTATUSLOADING)
        Observable.create<List<CollectRadioBean>> {
            it.onNext(MyApplication.collectRadioDao.queryAllCollectRadio())

        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<List<CollectRadioBean>>() {
                override fun onNext(t: List<CollectRadioBean>) {
                    dataResultListener.setQueryStatus(Constants.QUERYSTATUSSUCCESS)
                    dataResultListener.setAllCollectRadio(t)


                }

                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    dataResultListener.setQueryStatus(Constants.QUERYSTATUSFAILED)
                    dataResultListener.setErrorMsg(e.message.toString())
                }
            })

    }


    public fun onDestory() {}
}
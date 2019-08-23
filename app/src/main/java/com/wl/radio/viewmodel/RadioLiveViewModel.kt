package com.wl.radio.viewmodel

import androidx.lifecycle.*
import com.wl.radio.model.RadioLiveModel
import com.ximalaya.ting.android.opensdk.model.live.program.ProgramList
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList

class RadioLiveViewModel:ViewModel(),LifecycleObserver,RadioLiveModel.DataResultListener<RadioList>{


    var radioListLiveData:MutableLiveData<RadioList>?=MutableLiveData()
    var queryStatusLiveData:MutableLiveData<String> ?=MutableLiveData()
    var errorMsgLiveData:MutableLiveData<String>?=MutableLiveData()
    var radioInfoLiveData:MutableLiveData<ProgramList>?= MutableLiveData()

    val radioLiveModel:RadioLiveModel = RadioLiveModel(this)

    public fun getXmlyRadios(videoType:Int,provinceCode:Int,page:Int){
        radioLiveModel.getXmlyRadios(videoType,provinceCode,page)
    }

    public fun getRadioInfo(radioId:Long){
        radioLiveModel.getRadioInfo(radioId)
    }

    override fun setRadioInfo(radioInfo: ProgramList) {

     radioInfoLiveData?.value=radioInfo
    }

    override fun radioListSuccess(radiolist: RadioList) {
        radioListLiveData?.value =radiolist
    }

    override fun setQueryStatus(status: String) {
        queryStatusLiveData?.value = status
    }

    override fun setErrorMsg(msg: String) {
        errorMsgLiveData?.value=msg
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestory(){

    }



}
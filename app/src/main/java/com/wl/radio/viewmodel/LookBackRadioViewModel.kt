package com.wl.radio.viewmodel

import androidx.lifecycle.*
import com.wl.radio.model.LookBackRadioModel
import com.ximalaya.ting.android.opensdk.model.live.schedule.ScheduleList

class LookBackRadioViewModel:ViewModel(),LifecycleObserver , LookBackRadioModel.DataResultListener<ScheduleList>{


    var lookBackProgramsLiveData = MutableLiveData<ScheduleList>()
    var queryStatusLiveData:MutableLiveData<String> ?=MutableLiveData()
    var errorMsgLiveData:MutableLiveData<String>?=MutableLiveData()
    var lookBackRadioModel = LookBackRadioModel(this)


    override fun lookBackPragramsListSuccess(scheduleList: ScheduleList) {
        lookBackProgramsLiveData.value= scheduleList

    }

    override fun setQueryStatus(queryStatus:String){
        queryStatusLiveData?.value = queryStatus
    }

    override fun setErrorMsg(errorMsg:String){
        errorMsgLiveData?.value = errorMsg
    }


    fun getLookBackPrograms(radioId:String,dayOfWeek:String){
        lookBackRadioModel.getLookBackPrograms(radioId,dayOfWeek)

    }




    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestory(){

    }
}
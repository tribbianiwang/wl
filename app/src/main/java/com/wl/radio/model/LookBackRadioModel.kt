package com.wl.radio.model

import android.util.Log
import com.wl.radio.util.Constants
import com.wl.radio.viewmodel.LookBackRadioViewModel
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.live.program.ProgramList
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList
import com.ximalaya.ting.android.opensdk.model.live.schedule.ScheduleList

class LookBackRadioModel( lookBackRadioViewModel: LookBackRadioViewModel) {
    public interface DataResultListener<ScheduleList>{
        fun lookBackPragramsListSuccess(scheduleList: com.ximalaya.ting.android.opensdk.model.live.schedule.ScheduleList)
        fun setQueryStatus(status:String)
        fun setErrorMsg(msg:String)


    }

    public var dataResultListener: DataResultListener<ScheduleList> ? = null

    init {
        dataResultListener=lookBackRadioViewModel
    }

    fun getLookBackPrograms(radioId:String,dayOfWeek:String){
        Log.d("lookbackRadioModel",radioId+"id"+dayOfWeek)
        var map : HashMap<String, String>  =  HashMap<String, String>();

        map.put(DTransferConstants.RADIOID, radioId);

        map.put(DTransferConstants.WEEKDAY, dayOfWeek);

        CommonRequest.getSchedules(map,object: IDataCallBack<ScheduleList> {
            override fun onSuccess(scheduleList: ScheduleList?) {
                if(scheduleList?.getmScheduleList()!=null&&scheduleList.getmScheduleList().size>0){
                    dataResultListener?.setQueryStatus(Constants.QUERYSTATUSSUCCESS)
                    dataResultListener?.lookBackPragramsListSuccess(scheduleList)
                }else{
                    dataResultListener?.setQueryStatus(Constants.QUERYSTATUSEMPTY)
                }

            }

            override fun onError(p0: Int,errorMsg: String?) {
                dataResultListener?.setQueryStatus(Constants.QUERYSTATUSFAILED)
                errorMsg?.let { dataResultListener?.setErrorMsg(it) }

            }

        });

    }
}
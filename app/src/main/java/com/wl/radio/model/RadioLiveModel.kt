package com.wl.radio.model

import com.wl.radio.util.Constants.QUERYSTATUSFAILED
import com.wl.radio.util.Constants.QUERYSTATUSLOADING
import com.wl.radio.util.Constants.QUERYSTATUSSUCCESS
import com.wl.radio.util.LogUtils
import com.wl.radio.viewmodel.RadioLiveViewModel
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList
import com.ximalaya.ting.android.opensdk.model.live.program.ProgramList




class RadioLiveModel(radioLiveViewModel: DataResultListener<RadioList>) {



    public fun onDestory(){}

    public interface DataResultListener<RadioList>{
        fun radioListSuccess(radiolist: com.ximalaya.ting.android.opensdk.model.live.radio.RadioList)
        fun setQueryStatus(status:String)
        fun setErrorMsg(msg:String)
        fun setRadioInfo(radioInfo:ProgramList)
    }

    public var dataResultListener: DataResultListener<RadioList> ? = null

    init {
        dataResultListener=radioLiveViewModel
    }


    public fun getXmlyRadios(videoType:Int,provinceCode:Int,page:Int) {
        dataResultListener?.setQueryStatus(QUERYSTATUSLOADING)
        val map = mapOf<String,String>(Pair(DTransferConstants.RADIOTYPE, videoType.toString()),
            Pair(DTransferConstants.PROVINCECODE,provinceCode.toString()), Pair(DTransferConstants.PAGE, page.toString())
        )

        CommonRequest.getRadios(map, object : IDataCallBack<RadioList> {
            override fun onSuccess(radiolist: RadioList?) {
                dataResultListener?.setQueryStatus(QUERYSTATUSSUCCESS)
                radiolist?.let { dataResultListener?.radioListSuccess(it) }

            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                dataResultListener?.setQueryStatus(QUERYSTATUSFAILED)
                errorMsg?.let { dataResultListener?.setErrorMsg(it) }
            }

        });



    }


    public fun getRadioInfo(radioId:Long){
        dataResultListener?.setQueryStatus(QUERYSTATUSLOADING)
        val map = mapOf<String,String>(Pair(DTransferConstants.RADIOID,radioId.toString())
        )
        CommonRequest.getProgram(map, object:IDataCallBack<ProgramList>{
            override fun onSuccess(programList: ProgramList?) {
                dataResultListener?.setQueryStatus(QUERYSTATUSSUCCESS)
                programList?.let { dataResultListener?.setRadioInfo(it) }
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                dataResultListener?.setQueryStatus(QUERYSTATUSFAILED)
            }
        })

    }


}
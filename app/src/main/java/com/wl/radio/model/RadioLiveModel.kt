package com.wl.radio.model

import com.wl.radio.util.Constants
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
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioListById


class RadioLiveModel(radioLiveViewModel: DataResultListener<RadioList>) {





    public interface DataResultListener<RadioList>{
        fun radioListSuccess(radiolist: com.ximalaya.ting.android.opensdk.model.live.radio.RadioList)
        fun setQueryStatus(status:String)
        fun setErrorMsg(msg:String)
        fun setRadioInfo(radioInfo:ProgramList)
        fun setRadioListByIdsSuccess(radiolist:List<Radio>)

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
    public fun onDestory(){}


    fun getRadioInfos(radioDataIds:String){
        val map = HashMap<String, String>()

        map[DTransferConstants.RADIO_IDS] = radioDataIds.toString()

        CommonRequest.getRadiosByIds(map, object :IDataCallBack<RadioListById>{
            override fun onSuccess(radioLists: RadioListById?) {
                if(radioLists==null||radioLists.radios==null||radioLists.radios.size==0){
                    dataResultListener?.setQueryStatus(Constants.QUERYSTATUSEMPTY)
                }else{
                    dataResultListener?.setQueryStatus(Constants.QUERYSTATUSSUCCESS)
                    dataResultListener?.setRadioListByIdsSuccess(radioLists.radios)
                }



            }

            override fun onError(errorType: Int, errorMsg: String?) {
                dataResultListener?.setQueryStatus(Constants.QUERYSTATUSFAILED)
                errorMsg?.let { dataResultListener?.setErrorMsg(it) }

            }

        })

    }

}
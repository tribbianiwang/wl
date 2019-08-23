package com.wl.radio.model

import com.wl.radio.util.Constants
import com.wl.radio.util.Constants.DEFAULTRANKNUM
import com.wl.radio.util.Constants.PULLTOREFRESHFIRSTGET
import com.wl.radio.util.Constants.PULLTOREFRESHLOADMORE
import com.wl.radio.util.Constants.PULLTOREFRESHREFRESH
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest.getRankRadios
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList
import com.ximalaya.ting.android.opensdk.model.live.provinces.ProvinceList
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioCategoryList
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioListByCategory








class RadioPullRefreshModel(dataResultListener: DataResultListener) {

    internal var dataResultListener: DataResultListener

    init {
        this.dataResultListener = dataResultListener
    }

    public fun onDestory(){

    }


    interface DataResultListener {

        fun setQueryStatus(status: String)

        fun setQueryListSuccess(radioList: RadioList)

        fun setRefreshSuccess(radioList: RadioList)

        fun setLoadMoreSuccess(radioList: RadioList)

        fun setProvinceList(provinceList: ProvinceList)

        fun setErrorMsg(errorMsg:String)

        fun setCategoryList(categoryList: RadioCategoryList)

        fun setCategoryRadioQuerySuccess(radioCategoryList: RadioListByCategory)
        fun setCategoryRadioRefreshSuccess(radioCategoryList: RadioListByCategory)
        fun setCategoryRadioLoadMoreSuccess(radioCategoryList: RadioListByCategory)

        fun setRankRadioListSuccess(radioList: RadioList);


    }


    public fun getXmlyRadios(videoType: Int, provinceCode: Int, page: Int, pulltorefreshtype: Int) {
        if(PULLTOREFRESHFIRSTGET==pulltorefreshtype){
            dataResultListener?.setQueryStatus(Constants.QUERYSTATUSLOADING)
        }

        val map = mapOf<String,String>(Pair(DTransferConstants.RADIOTYPE, videoType.toString()),
            Pair(DTransferConstants.PROVINCECODE,provinceCode.toString()), Pair(DTransferConstants.PAGE, page.toString())
        )

        CommonRequest.getRadios(map, object : IDataCallBack<RadioList> {
            override fun onSuccess(radiolist: RadioList?) {
                dataResultListener?.setQueryStatus(Constants.QUERYSTATUSSUCCESS)

                when(pulltorefreshtype){
                    PULLTOREFRESHFIRSTGET->radiolist?.let { dataResultListener?.setQueryListSuccess(it) }
                    PULLTOREFRESHREFRESH->radiolist?.let { dataResultListener?.setRefreshSuccess(it) }
                    PULLTOREFRESHLOADMORE->radiolist?.let { dataResultListener?.setLoadMoreSuccess(it) }
                }


            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                dataResultListener?.setQueryStatus(Constants.QUERYSTATUSFAILED)
                errorMsg?.let { dataResultListener?.setErrorMsg(it) }
            }

        });



    }







    public fun getProvinceList(){
                dataResultListener?.setQueryStatus(Constants.QUERYSTATUSLOADING)
        val map = HashMap<String, String>()
        CommonRequest.getProvinces(map,object : IDataCallBack<ProvinceList>{
            override fun onSuccess(provinceList: ProvinceList?) {
                dataResultListener?.setQueryStatus(Constants.QUERYSTATUSSUCCESS)
                provinceList?.let { dataResultListener?.setProvinceList(it) }
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                dataResultListener?.setQueryStatus(Constants.QUERYSTATUSFAILED)
                errorMsg?.let { dataResultListener?.setErrorMsg(it) }
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }


    public fun getRadioCategories(){
        dataResultListener?.setQueryStatus(Constants.QUERYSTATUSLOADING)

        val map = HashMap<String, String>()
        CommonRequest.getRadioCategory(map, object : IDataCallBack<RadioCategoryList> {
            override fun onSuccess(categoryList: RadioCategoryList?) {
                dataResultListener?.setQueryStatus(Constants.QUERYSTATUSSUCCESS)
                if (categoryList != null) {
                    dataResultListener?.setCategoryList(categoryList)
                }
            }

            override fun onError(code: Int, message: String) {
                dataResultListener?.setQueryStatus(Constants.QUERYSTATUSFAILED)
            }
        })

    }

    public fun getRadioCategoryList(radioCategoryId:Int,page:Int,pulltorefreshtype: Int){
        if(PULLTOREFRESHFIRSTGET==pulltorefreshtype){
            dataResultListener?.setQueryStatus(Constants.QUERYSTATUSLOADING)
        }
        val maps = HashMap<String, String>()
        maps[DTransferConstants.RADIO_CATEGORY_ID] = radioCategoryId.toString()
        maps[DTransferConstants.PAGE] = page.toString()
        CommonRequest.getRadiosByCategory(maps, object : IDataCallBack<RadioListByCategory> {
            override fun onSuccess(radioListByCateGory: RadioListByCategory?) {
                dataResultListener?.setQueryStatus(Constants.QUERYSTATUSSUCCESS)

                when(pulltorefreshtype){
                    PULLTOREFRESHFIRSTGET->radioListByCateGory?.let { dataResultListener?.setCategoryRadioQuerySuccess(it) }
                    PULLTOREFRESHREFRESH->radioListByCateGory?.let { dataResultListener?.setCategoryRadioRefreshSuccess(it) }
                    PULLTOREFRESHLOADMORE->radioListByCateGory?.let { dataResultListener?.setCategoryRadioLoadMoreSuccess(it) }
                }

            }

            override fun onError(code: Int, message: String) {
                dataResultListener?.setQueryStatus(Constants.QUERYSTATUSFAILED)
                message?.let { dataResultListener?.setErrorMsg(it) }
            }
        })
    }

    public fun getRankRadioList(){
        dataResultListener?.setQueryStatus(Constants.QUERYSTATUSLOADING)
        val map = HashMap<String, String>()
        map[DTransferConstants.RADIO_COUNT] = DEFAULTRANKNUM.toString()
        getRankRadios(map,object :IDataCallBack<RadioList>{
            override fun onSuccess(radioList: RadioList?) {
                dataResultListener?.setQueryStatus(Constants.QUERYSTATUSSUCCESS)
                radioList?.let { dataResultListener?.setRankRadioListSuccess(it) }
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                dataResultListener?.setQueryStatus(Constants.QUERYSTATUSFAILED)
                errorMsg?.let { dataResultListener?.setErrorMsg(it) }
            }

        })
    }




}
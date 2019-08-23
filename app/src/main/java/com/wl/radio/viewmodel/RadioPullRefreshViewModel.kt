package com.wl.radio.viewmodel

import androidx.lifecycle.*
import com.wl.radio.model.RadioPullRefreshModel
import com.wl.radio.util.Constants.PULLTOREFRESHFIRSTGET
import com.wl.radio.util.Constants.PULLTOREFRESHLOADMORE
import com.wl.radio.util.Constants.PULLTOREFRESHREFRESH
import com.wl.radio.util.LogUtils
import com.ximalaya.ting.android.opensdk.model.live.provinces.ProvinceList
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioCategoryList
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioListByCategory

class RadioPullRefreshViewModel :ViewModel(),LifecycleObserver,RadioPullRefreshModel.DataResultListener{


    override fun setCategoryList(categoryList: RadioCategoryList) {
//        radioPullRefreshModel.
        radioCategoryListLiveData?.value=categoryList
    }

    var TAG="RadioPullRefreshViewModel"

    var queryListSuccessLiveData: MutableLiveData<RadioList>?= MutableLiveData()
    var refreshListSuccessLiveData: MutableLiveData<RadioList>?= MutableLiveData()
    var loadmoreListSuccessLiveData: MutableLiveData<RadioList>?= MutableLiveData()
    var queryStatusLiveData: MutableLiveData<String>?= MutableLiveData()
    var errorMsgLiveData:MutableLiveData<String>?= MutableLiveData()
    var provinceListLiveData:MutableLiveData<ProvinceList>?= MutableLiveData()
    var radioCategoryListLiveData:MutableLiveData<RadioCategoryList>?=MutableLiveData()
    var rankListSuccessLiveData:MutableLiveData<RadioList>?= MutableLiveData()




    var radioPullRefreshModel : RadioPullRefreshModel = RadioPullRefreshModel(this)


    public fun getRadioCategoriesList(){
        radioPullRefreshModel.getRadioCategories()
    }

    public fun getXmlyRadios(videoType:Int,provinceCode:Int,page:Int){

        radioPullRefreshModel.getXmlyRadios(videoType,provinceCode,page,PULLTOREFRESHFIRSTGET)

    }

    public fun getCategoryRadios(radioCategoryId:Int,page:Int){
        LogUtils.d(TAG,"getcategorys"+radioCategoryId)
        radioPullRefreshModel.getRadioCategoryList(radioCategoryId,page,PULLTOREFRESHFIRSTGET)
    }

    public fun getRankRadioList(){
        radioPullRefreshModel.getRankRadioList()

    }

    public fun getProvinceList(){
        radioPullRefreshModel.getProvinceList()
    }


    public fun refleshXmlyRadios(videoType:Int,provinceCode:Int,page:Int){
        radioPullRefreshModel.getXmlyRadios(videoType,provinceCode,page,PULLTOREFRESHREFRESH)
    }

    public fun refreshCategoryRadios(radioCategoryId:Int,page:Int){
        radioPullRefreshModel.getRadioCategoryList(radioCategoryId,page,PULLTOREFRESHREFRESH)
    }


    public fun loadmoreXmlyRadios(videoType:Int,provinceCode:Int,page:Int){
        radioPullRefreshModel.getXmlyRadios(videoType,provinceCode,page,PULLTOREFRESHLOADMORE)
    }


    public fun loadmoreCategoryRadios(radioCategoryId:Int,page:Int){
        radioPullRefreshModel.getRadioCategoryList(radioCategoryId,page,PULLTOREFRESHLOADMORE)
    }


    override fun setQueryStatus(status: String) {
        queryStatusLiveData?.value=status
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestory(){
        radioPullRefreshModel.onDestory()
    }
    override fun setQueryListSuccess(radioList: RadioList) {
        queryListSuccessLiveData?.value=radioList
    }

    override fun setRefreshSuccess(radioList: RadioList) {
        refreshListSuccessLiveData?.value=radioList
    }

    override fun setLoadMoreSuccess(radioList: RadioList) {
        loadmoreListSuccessLiveData?.value = radioList
    }

    override fun setErrorMsg(errorMsg: String) {
        errorMsgLiveData?.value=errorMsg
        LogUtils.d(TAG,"error"+errorMsg)

    }

    override fun setProvinceList(provinceList: ProvinceList) {
        LogUtils.d(TAG,"provincelistsize"+provinceList?.provinceList.size)
        provinceListLiveData?.value=provinceList
    }


    override fun setCategoryRadioQuerySuccess(radioCategoryList: RadioListByCategory) {
         setQueryListSuccess(transRadioListByCategoryToRadioList(radioCategoryList))


    }

    public fun transRadioListByCategoryToRadioList(radioCategoryList: RadioListByCategory):RadioList{
        var radioList: RadioList = RadioList()
        radioList.radios = radioCategoryList.radios
        radioList.totalPage = radioCategoryList.totalPage
        radioList.currentPage= radioCategoryList.currentPage
        return radioList
    }



    override fun setCategoryRadioRefreshSuccess(radioCategoryList: RadioListByCategory) {
        setRefreshSuccess(transRadioListByCategoryToRadioList(radioCategoryList))
    }

    override fun setCategoryRadioLoadMoreSuccess(radioCategoryList: RadioListByCategory) {
        setLoadMoreSuccess(transRadioListByCategoryToRadioList(radioCategoryList))
    }

    override fun setRankRadioListSuccess(radioList: RadioList) {
        rankListSuccessLiveData?.value = radioList
    }

}
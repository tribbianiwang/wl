package com.wl.radio.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wl.radio.R
import com.wl.radio.adapter.RvHomeAdapter
import com.wl.radio.util.*
import com.wl.radio.util.Constants.CITYCODE
import com.wl.radio.util.Constants.ISQUERYCATEGORY
import com.wl.radio.util.Constants.RADIOCATEGORYID
import com.wl.radio.util.Constants.RADIOTYPE
import com.wl.radio.util.Constants.TRANSRADIO
import com.wl.radio.viewmodel.RadioPullRefreshViewModel
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import kotlinx.android.synthetic.main.activity_normal_radio_list.*
import java.util.ArrayList

class NormalRadioListActivity : BaseActivity() {
    var radioPullRefreshViewModel:RadioPullRefreshViewModel?=null
    val TAG:String="NormalRadioListActivity"
    internal var isPullRefresh = false
    private var page = 1
    private var radioCategoryId:Int=0

    private var reRecordLists: MutableList<Radio>? = null
    private var rvAdapter:RvHomeAdapter?=null
    private var cityCode=410000
    private var radioType=2
    var isQueryCategory:Boolean=false
    var mPlayerManager: XmPlayerManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_radio_list)

        mPlayerManager = XmPlayerManager.getInstance(this)
         cityCode =intent.getIntExtra(CITYCODE,410000)
        radioType = intent.getIntExtra(RADIOTYPE,1)
        isQueryCategory = intent.getBooleanExtra(ISQUERYCATEGORY,false)
        radioCategoryId = intent.getLongExtra(RADIOCATEGORYID,0).toInt()


        //1.
        radioPullRefreshViewModel = ViewModelProviders.of(this).get(RadioPullRefreshViewModel::class.java)

        //2
        lifecycle.addObserver(radioPullRefreshViewModel!!)

        //3
        val queryStatusObserver: Observer<String> = Observer {

            when(it){
                Constants.QUERYSTATUSLOADING -> showLoading()
                Constants.QUERYSTATUSFAILED -> showLoadFailed()
                Constants.QUERYSTATUSSUCCESS -> showLoadSuccess()
            }

        }
        val errorMsgObserver:Observer<String> = Observer {
            LogUtils.e(TAG,it)

        }


        val radioListResultObserver:Observer<RadioList> = Observer{
            if (it.radios.size=== 0) {
                showEmpty()
            } else {
                showLoadSuccess()

                setAdatper(it)
            }
        }


        val radioListRefreshObserver:Observer<RadioList> = Observer{
            refreshComplete(true)
            setAdatper(it)
        }

        val radioListLoadMoreObserver:Observer<RadioList> = Observer{
            loadmoreComplete(true)
            addData(it)
        }

        //4
        radioPullRefreshViewModel?.queryListSuccessLiveData?.observe(this,radioListResultObserver)
        radioPullRefreshViewModel?.refreshListSuccessLiveData?.observe(this,radioListRefreshObserver)
        radioPullRefreshViewModel?.loadmoreListSuccessLiveData?.observe(this,radioListLoadMoreObserver)
        radioPullRefreshViewModel?.errorMsgLiveData?.observe(this,errorMsgObserver)
        radioPullRefreshViewModel?.queryStatusLiveData?.observe(this,queryStatusObserver)


        if(isQueryCategory){
            radioPullRefreshViewModel?.getCategoryRadios(radioCategoryId,page)
        }else{
            radioPullRefreshViewModel?.getXmlyRadios(radioType,cityCode,page)
        }

        refreshLayout.setEnableRefresh(true)
        refreshLayout.setEnableLoadMore(true)

        refreshLayout.setOnRefreshListener {
            isPullRefresh = true
            page = 1
            if(isQueryCategory){
                radioPullRefreshViewModel?.refreshCategoryRadios(radioCategoryId,page)
            }else{
                radioPullRefreshViewModel?.refleshXmlyRadios(radioType,cityCode,page)
            }



        }

        refreshLayout.setOnLoadMoreListener {
            isPullRefresh = true
            page++

            if(isQueryCategory){
                radioPullRefreshViewModel?.loadmoreCategoryRadios(radioCategoryId,page)
            }else{
                radioPullRefreshViewModel?.loadmoreXmlyRadios(radioType,cityCode,page)
            }


        }


    }

    override fun onLoadRetry() {
        // override this method in subclass to do retry task
        if(isQueryCategory){
            radioPullRefreshViewModel?.getCategoryRadios(radioCategoryId,page)
        }else{
            radioPullRefreshViewModel?.getXmlyRadios(radioType,cityCode,page)
        }
    }


    fun nomore(isLastPage: Boolean) {
        //        pdrAndPulView.setNomore(true);
        refreshLayout.finishLoadMore()
        if (isLastPage) {
            T.showShort(this, StringUtils.getRString(this, R.string.no_more_data))
        }
        //
    }


    fun addData(radioList: RadioList) {
        if (radioList.radios.size === 0) {
            nomore(true)
            return
        }else{


            nomore(false)

            radioList?.radios?.let { (reRecordLists as ArrayList<Radio>).addAll(it) }
            rvAdapter?.notifyDataSetChanged()
            return
        }




    }


    private fun setAdatper(recordList: RadioList?) {


        page = 1
        reRecordLists = ArrayList<Radio>()
        recordList?.radios?.let { (reRecordLists as ArrayList<Radio>).addAll(it) }
        rvAdapter = RvHomeAdapter(this,reRecordLists)
        rvAdapter?.selectDataId= mPlayerManager?.currSound?.dataId?:0

        rvRadioList.setAdapter(rvAdapter)
        rvRadioList.setLayoutManager(LinearLayoutManager(this))

        refreshComplete(true)

        rvAdapter!!.setOnItemClickListener(object : RvItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                var intent: Intent = Intent(this@NormalRadioListActivity,PlayingActivity::class.java)
                rvAdapter?.selectDataId = reRecordLists?.get(position)?.dataId?:0

                rvAdapter?.notifyDataSetChanged()
                intent.putExtra(TRANSRADIO,reRecordLists?.get(position))


                startActivity(intent)


            }
        })

    }


    fun refreshComplete(success: Boolean) {
        refreshLayout.finishRefresh()
        //        pdrAndPulView.refreshComplete(success);
    }

    fun loadmoreComplete(success: Boolean) {
        refreshLayout.finishLoadMore()
        //        pdrAndPulView.loadmoreComplete(success);
        if (success) {

        } else {
            page--
        }


    }

}

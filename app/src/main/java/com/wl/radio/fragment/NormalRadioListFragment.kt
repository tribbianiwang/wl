package com.wl.radio.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.billy.android.loading.Gloading

import com.wl.radio.R
import com.wl.radio.activity.PlayingActivity
import com.wl.radio.adapter.RvHomeAdapter
import com.wl.radio.util.*
import com.wl.radio.util.Constants.CITYCODE
import com.wl.radio.util.Constants.CITYNAME
import com.wl.radio.viewmodel.RadioPullRefreshViewModel
import com.ximalaya.ting.android.opensdk.model.live.provinces.Province
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import kotlinx.android.synthetic.main.activity_normal_radio_list.*
import kotlinx.android.synthetic.main.fragment_normal_radio_list.view.*
import java.util.ArrayList
import android.content.BroadcastReceiver

import android.content.Context
import com.wl.radio.util.Constants.BROADCASTSELECTRADIOIDCHANGE
import android.content.IntentFilter
import com.wl.radio.MyApplication
import com.wl.radio.util.Constants.SELECTRADIOID



class NormalRadioListFragment : Fragment() {
    var cityName:String?=null
    var cityCode:Int?=0
    private var holder: Gloading.Holder? = null


    var radioPullRefreshViewModel: RadioPullRefreshViewModel?=null
    val TAG:String="NormalRadioListActivity"
    internal var isPullRefresh = false
    private var page = 1
    private var reRecordLists: MutableList<Radio>? = null
    private var rvAdapter: RvHomeAdapter?=null

    private var radioType=2
    var mPlayerManager: XmPlayerManager? = null


    var mainReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (intent.action == BROADCASTSELECTRADIOIDCHANGE) {
                    var selecRadioId = intent.getLongExtra(SELECTRADIOID,0)

                LogUtils.d(TAG,"selectradioId"+selecRadioId)
                rvAdapter?.selectDataId = selecRadioId
                rvAdapter?.notifyDataSetChanged()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                var view:View = View.inflate(context,R.layout.fragment_normal_radio_list,null)
        holder = Gloading.getDefault().wrap(view).withRetry(Runnable {
            //change picture url to a correct one
            onLoadRetry()
        })
        cityName= arguments?.getString(CITYNAME,"213443")
        cityCode = arguments?.getLong(CITYCODE,0)?.toInt()






        return holder?.getWrapper()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        mPlayerManager = XmPlayerManager.getInstance(context)

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
        val errorMsgObserver: Observer<String> = Observer {
            LogUtils.e(TAG,it)

        }


        val radioListResultObserver: Observer<RadioList> = Observer{
            if (it.radios.size=== 0) {
                showEmpty()
            } else {
                showLoadSuccess()

                setAdatper(it)
            }
        }


        val radioListRefreshObserver: Observer<RadioList> = Observer{
            refreshComplete(true)
            setAdatper(it)
        }

        val radioListLoadMoreObserver: Observer<RadioList> = Observer{
            loadmoreComplete(true)
            addData(it)
        }

        //4
        radioPullRefreshViewModel?.queryListSuccessLiveData?.observe(this,radioListResultObserver)
        radioPullRefreshViewModel?.refreshListSuccessLiveData?.observe(this,radioListRefreshObserver)
        radioPullRefreshViewModel?.loadmoreListSuccessLiveData?.observe(this,radioListLoadMoreObserver)
        radioPullRefreshViewModel?.errorMsgLiveData?.observe(this,errorMsgObserver)
        radioPullRefreshViewModel?.queryStatusLiveData?.observe(this,queryStatusObserver)

        cityCode?.let { radioPullRefreshViewModel?.getXmlyRadios(radioType, it,page) }
        refreshLayout.setEnableRefresh(true)
        refreshLayout.setEnableLoadMore(true)

        refreshLayout.setOnRefreshListener {
            isPullRefresh = true
            page = 1
            cityCode?.let { it1 -> radioPullRefreshViewModel?.refleshXmlyRadios(radioType, it1,page) }


        }

        refreshLayout.setOnLoadMoreListener {
            isPullRefresh = true
            page++
            cityCode?.let { it1 -> radioPullRefreshViewModel?.loadmoreXmlyRadios(radioType, it1,page) }

        }


        val intentFilter = IntentFilter()
        intentFilter.addAction(BROADCASTSELECTRADIOIDCHANGE)

        activity?.registerReceiver(mainReceiver, intentFilter)
    }


    open fun onLoadRetry() {
        // override this method in subclass to do retry task
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

    private fun setAdatper(recordList: RadioList?) {


        page = 1
        reRecordLists = ArrayList<Radio>()
        recordList?.radios?.let { (reRecordLists as ArrayList<Radio>).addAll(it) }
        rvAdapter = RvHomeAdapter(context,reRecordLists)
        rvAdapter?.selectDataId= mPlayerManager?.currSound?.dataId?:0

        rvRadioList.setAdapter(rvAdapter)
        rvRadioList.setLayoutManager(LinearLayoutManager(context))

        refreshComplete(true)

        rvAdapter!!.setOnItemClickListener(object : RvItemClickListener {
            override fun onItemClick(view: View, position: Int) {

//                rvAdapter?.selectDataId = reRecordLists?.get(position)?.dataId?:0
//                rvAdapter?.notifyDataSetChanged()

                var broadcastIntent :Intent = Intent();
                broadcastIntent.action=BROADCASTSELECTRADIOIDCHANGE

                LogUtils.d(TAG,"selectradioId-"+(reRecordLists?.get(position)?.dataId?:0))
                broadcastIntent.putExtra(SELECTRADIOID,reRecordLists?.get(position)?.dataId?:0)
                activity?.sendBroadcast(broadcastIntent)


                MyApplication.refreshRadioList(reRecordLists as ArrayList<Radio>)
                var intent: Intent = Intent(activity, PlayingActivity::class.java)
                intent.putExtra(Constants.TRANSRADIO,reRecordLists?.get(position))
                startActivity(intent)




            }
        })

    }


    fun showLoading() {

        holder?.showLoading()
    }

    fun showLoadSuccess() {

        holder?.showLoadSuccess()
    }

    fun showLoadFailed() {

        holder?.showLoadFailed()
    }

    fun showEmpty() {

        holder?.showEmpty()
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

    fun nomore(isLastPage: Boolean) {
        //        pdrAndPulView.setNomore(true);
        refreshLayout.finishLoadMore()
        if (isLastPage) {
            activity?.let { context?.let { StringUtils.getRString(it,R.string.no_more_data) }?.let { it1 ->
                T.showShort(it,
                    it1
                )
            } }
        }
        //
    }


    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(mainReceiver)
    }



}

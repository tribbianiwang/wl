package com.wl.radio.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wl.radio.MyApplication
import com.wl.radio.R
import com.wl.radio.activity.NormalRadioListActivity
import com.wl.radio.activity.PlayingActivity
import com.wl.radio.activity.ProvinceRadioListActivity
import com.wl.radio.adapter.RvHomeAdapter
import com.wl.radio.util.Constants
import com.wl.radio.util.Constants.BROADCAST_REFRESH_PLAY_RADIO_HISTORY
import com.wl.radio.util.Constants.CITYCODE
import com.wl.radio.util.Constants.CITYCODEDEFAULT
import com.wl.radio.util.Constants.CITYCODEHENAN
import com.wl.radio.util.Constants.RADIOTYPE
import com.wl.radio.util.Constants.RADIOTYPECOUNTRY
import com.wl.radio.util.Constants.RADIOTYPEINTERNET
import com.wl.radio.util.Constants.RADIOTYPEPROVINCE
import com.wl.radio.util.Constants.TRANS_PLAYING_RADIO
import com.wl.radio.util.LogUtils
import com.wl.radio.util.RvItemClickListener
import com.wl.radio.viewmodel.RadioPullRefreshViewModel
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList
import kotlinx.android.synthetic.main.layout_fragment_home.*
import kotlinx.android.synthetic.main.layout_home_rv_head.*


class HomeFragment : BaseFragment() {

    var innerBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Constants.BROADCAST_REFRESH_PLAY_RADIO_HISTORY -> {
                    Log.d(TAG,"start_refresh_play_radio")
                    setRvHistoryAdapter()

                    playingRadio = intent.getParcelableExtra<Radio>(TRANS_PLAYING_RADIO)
                    playingRadio?.dataId?.let { refreshWaveAnim(it) }
                }
            }

        }

    }


    val TAG = HomeFragment::class.java.simpleName
    var radioPullRefreshViewModel: RadioPullRefreshViewModel? = null

    var selectRadioId:Long=0

    var playingRadio:Radio?=null
    var rvHistoryAdapter: RvHomeAdapter? = null
    var rvCityAdapter: RvHomeAdapter? = null
    var rvRankAdapter: RvHomeAdapter? = null


    var playRadioHistoryList: ArrayList<Radio> = ArrayList<Radio>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var intentFilter = IntentFilter()
        intentFilter.addAction(BROADCAST_REFRESH_PLAY_RADIO_HISTORY)
        context?.registerReceiver(innerBroadcastReceiver, intentFilter)

        rvPlayHistory.layoutManager = LinearLayoutManager(context);
        rvCity.layoutManager = LinearLayoutManager(context);
        rvRank.layoutManager = LinearLayoutManager(context);


        setRvHistoryAdapter()

        llRadioLocal.setOnClickListener {
            var intent: Intent = Intent(activity, NormalRadioListActivity::class.java)
            intent.putExtra(CITYCODE, CITYCODEHENAN)
            intent.putExtra(RADIOTYPE, RADIOTYPEPROVINCE)
            startActivity(intent)
        }

        llRadioCountry.setOnClickListener {
            var intent: Intent = Intent(activity, NormalRadioListActivity::class.java)
            intent.putExtra(CITYCODE, CITYCODEDEFAULT)
            intent.putExtra(RADIOTYPE, RADIOTYPECOUNTRY)
            startActivity(intent)

        }

        llRadioProvince.setOnClickListener {

            startActivity(Intent(activity, ProvinceRadioListActivity::class.java))


        }

        llRadioInternet.setOnClickListener {
            var intent: Intent = Intent(activity, NormalRadioListActivity::class.java)
            intent.putExtra(CITYCODE, CITYCODEDEFAULT)
            intent.putExtra(RADIOTYPE, RADIOTYPEINTERNET)
            startActivity(intent)

        }


        rlCityMore.setOnClickListener {
            var intent: Intent = Intent(activity, NormalRadioListActivity::class.java)
            intent.putExtra(CITYCODE, CITYCODEHENAN)
            intent.putExtra(RADIOTYPE, RADIOTYPEPROVINCE)
            startActivity(intent)


        }


        //1.
        radioPullRefreshViewModel =
            ViewModelProviders.of(this).get(RadioPullRefreshViewModel::class.java)

        //2
        lifecycle.addObserver(radioPullRefreshViewModel!!)


        //3
        val queryStatusObserver: Observer<String> = Observer {

            when (it) {
//                Constants.QUERYSTATUSLOADING -> showLoading()
//                Constants.QUERYSTATUSFAILED -> showLoadFailed()
//                Constants.QUERYSTATUSSUCCESS -> showLoadSuccess()
            }

        }

        val errorMsgObserver: Observer<String> = Observer {
            //            LogUtils.e(TAG,it)

        }


        val radioRankListObserver: Observer<RadioList> = Observer {
            if (it.radios.size === 0) {
//                showEmpty()
            } else {
//                showLoadSuccess()
                LogUtils.d(TAG,"rvrank-data-size:"+it?.radios?.size)

                rvRankAdapter = RvHomeAdapter(context, it?.radios)
                rvRank.adapter = rvRankAdapter

                LogUtils.d(TAG,"rvrank-data-size:after"+  (rvRankAdapter)?.itemCount)
                rvRankAdapter?.setOnItemClickListener(object :
                    RvItemClickListener {
                    override fun onItemClick(view: View, position: Int) {

                        it?.radios?.get(position)?.let { it1 -> toPlayingActivity(it1,it.radios as ArrayList<Radio>) }
                    }

                })
            }

        }


        val radioListResultObserver: Observer<RadioList> = Observer {
            if (it.radios.size === 0) {
//                showEmpty()
            } else {
//                showLoadSuccess()

                rvCityAdapter = RvHomeAdapter(context, it?.radios?.subList(0, 3))
                rvCity.adapter = rvCityAdapter

                rvCityAdapter?.setOnItemClickListener(object :
                    RvItemClickListener {
                    override fun onItemClick(view: View, position: Int) {

                        it?.radios?.get(position)?.let { it1 -> toPlayingActivity(it1,it.radios as ArrayList<Radio>) }
                    }

                })

            }
        }
        radioPullRefreshViewModel?.queryListSuccessLiveData?.observe(this, radioListResultObserver)
        radioPullRefreshViewModel?.rankListSuccessLiveData?.observe(this, radioRankListObserver)

        radioPullRefreshViewModel?.getXmlyRadios(
            Constants.RADIOTYPEPROVINCE,
            CITYCODEHENAN,
            Constants.DEFAULTPAGE
        )
        radioPullRefreshViewModel?.getRankRadioList()


    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

          var  contentView = View.inflate(context, R.layout.layout_fragment_home, null);





        return contentView
    }


    private fun setRvHistoryAdapter() {
        Log.d("HomeFragment:", "setRvHistoryAdapter-size:" + MyApplication.historyRadioList.size)
        if (MyApplication.historyRadioList.size == 0) {
            rl_play_history.visibility = View.GONE
            view_line_play_history.visibility = View.GONE
            rvPlayHistory.visibility = View.GONE
        } else {
            rl_play_history.visibility = View.VISIBLE
            view_line_play_history.visibility = View.VISIBLE
            rvPlayHistory.visibility = View.VISIBLE


            if (rvHistoryAdapter == null) {
                playRadioHistoryList.addAll(MyApplication.getHistoryRadios())
                rvHistoryAdapter = RvHomeAdapter(context, playRadioHistoryList)
                rvHistoryAdapter?.selectDataId = selectRadioId
                rvPlayHistory.adapter = rvHistoryAdapter





            } else {
                playRadioHistoryList.clear()
                playRadioHistoryList.addAll(MyApplication.getHistoryRadios())
                rvHistoryAdapter?.notifyDataSetChanged()


            }

            rvHistoryAdapter?.setOnItemClickListener(object :RvItemClickListener{
                override fun onItemClick(view: View, position: Int) {
                    toPlayingActivity(playRadioHistoryList[position],playRadioHistoryList)

                }

            })


        }


    }


    override fun onFragmentVisible() {

        if(rvRankAdapter!=null){
            LogUtils.d(TAG, "onFragmentVisible"+rvRankAdapter?.itemCount)
        }

    }

    override fun onFragmentInVisible() {
        LogUtils.d(TAG, "onFragmentInVisible")
    }

    public fun refreshWaveAnim(dataId: Long) {
        selectRadioId = dataId
        rvRankAdapter?.selectDataId = dataId
        rvCityAdapter?.selectDataId = dataId
        rvHistoryAdapter?.selectDataId = dataId
        rvRankAdapter?.notifyDataSetChanged()
        rvCityAdapter?.notifyDataSetChanged()
        rvHistoryAdapter?.notifyDataSetChanged()


    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(innerBroadcastReceiver)
    }

    fun toPlayingActivity(radio:Radio,radioList:ArrayList<Radio>){
        MyApplication.refreshRadioList(radioList)
        var intent: Intent = Intent(activity, PlayingActivity::class.java)
        intent.putExtra(Constants.TRANSRADIO, radio)
        startActivity(intent)
    }
}
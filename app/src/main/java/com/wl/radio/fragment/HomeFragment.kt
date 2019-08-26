package com.wl.radio.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import com.wl.radio.util.Constants.CITYCODE
import com.wl.radio.util.Constants.CITYCODEDEFAULT
import com.wl.radio.util.Constants.CITYCODEHENAN
import com.wl.radio.util.Constants.RADIOTYPE
import com.wl.radio.util.Constants.RADIOTYPECOUNTRY
import com.wl.radio.util.Constants.RADIOTYPEINTERNET
import com.wl.radio.util.Constants.RADIOTYPEPROVINCE
import com.wl.radio.util.LogUtils
import com.wl.radio.util.RvItemClickListener
import com.wl.radio.viewmodel.RadioPullRefreshViewModel
import com.ximalaya.ting.android.opensdk.model.live.provinces.ProvinceList
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList
import kotlinx.android.synthetic.main.layout_fragment_home.*
import kotlinx.android.synthetic.main.layout_home_rv_head.*

class HomeFragment : Fragment() {
    var radioPullRefreshViewModel: RadioPullRefreshViewModel?=null

    var contentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         contentView=View.inflate(context, R.layout.layout_fragment_home,null);




       return contentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rvPlayHistory.layoutManager=LinearLayoutManager(context);
        rvCity.layoutManager=LinearLayoutManager(context);
        rvRank.layoutManager=LinearLayoutManager(context);

//        rvPlayHistory.adapter=RvHomeAdapter(context, recordList)
//        rvCity.adapter=RvHomeAdapter(context, recordList)
//        rvRank.adapter=RvHomeAdapter(context, recordList)

        llRadioLocal.setOnClickListener{
            var intent:Intent = Intent(activity,NormalRadioListActivity::class.java)
            intent.putExtra(CITYCODE,CITYCODEHENAN)
            intent.putExtra(RADIOTYPE,RADIOTYPEPROVINCE)
            startActivity(intent)
        }

        llRadioCountry.setOnClickListener {
            var intent:Intent = Intent(activity,NormalRadioListActivity::class.java)
            intent.putExtra(CITYCODE,CITYCODEDEFAULT)
            intent.putExtra(RADIOTYPE,RADIOTYPECOUNTRY)
            startActivity(intent)

        }

        llRadioProvince.setOnClickListener {

            startActivity(Intent(activity,ProvinceRadioListActivity::class.java))


        }

        llRadioInternet.setOnClickListener {
            var intent:Intent = Intent(activity,NormalRadioListActivity::class.java)
            intent.putExtra(CITYCODE,CITYCODEDEFAULT)
            intent.putExtra(RADIOTYPE,RADIOTYPEINTERNET)
            startActivity(intent)

        }


        rlCityMore.setOnClickListener {
            var intent:Intent = Intent(activity,NormalRadioListActivity::class.java)
            intent.putExtra(CITYCODE,CITYCODEHENAN)
            intent.putExtra(RADIOTYPE,RADIOTYPEPROVINCE)
            startActivity(intent)


        }



        //1.
        radioPullRefreshViewModel = ViewModelProviders.of(this).get(RadioPullRefreshViewModel::class.java)

        //2
        lifecycle.addObserver(radioPullRefreshViewModel!!)


        //3
        val queryStatusObserver: Observer<String> = Observer {

            when(it){
//                Constants.QUERYSTATUSLOADING -> showLoading()
//                Constants.QUERYSTATUSFAILED -> showLoadFailed()
//                Constants.QUERYSTATUSSUCCESS -> showLoadSuccess()
            }

        }

        val errorMsgObserver:Observer<String> = Observer {
//            LogUtils.e(TAG,it)

        }


        val radioRankListObserver:Observer<RadioList> = Observer {
            if (it.radios.size=== 0) {
//                showEmpty()
            } else {
//                showLoadSuccess()

                rvRank.adapter=RvHomeAdapter(context, it?.radios)
                (rvRank.adapter as RvHomeAdapter).setOnItemClickListener(object :RvItemClickListener{
                    override fun onItemClick(view: View, position: Int) {

                        refreshWaveAnim(it?.radios?.get(position)?.dataId?:0)
                        MyApplication.refreshRadioList(it.radios as ArrayList<Radio>)
                        var intent: Intent = Intent(activity, PlayingActivity::class.java)

                        intent.putExtra(Constants.TRANSRADIO,it?.radios?.get(position))

                        startActivity(intent)
                    }

                })
            }

        }


        val radioListResultObserver:Observer<RadioList> = Observer{
            if (it.radios.size=== 0) {
//                showEmpty()
            } else {
//                showLoadSuccess()

                rvCity.adapter=    RvHomeAdapter(context, it?.radios?.subList(0,3))

                (rvCity.adapter as RvHomeAdapter).setOnItemClickListener(object :RvItemClickListener{
                    override fun onItemClick(view: View, position: Int) {


                        refreshWaveAnim(it?.radios?.get(position)?.dataId?:0)
                        MyApplication.refreshRadioList(it.radios as ArrayList<Radio>)
                        var intent: Intent = Intent(activity, PlayingActivity::class.java)
                        intent.putExtra(Constants.TRANSRADIO,it?.radios?.get(position))
                        startActivity(intent)
                    }

                })

            }
        }
        radioPullRefreshViewModel?.queryListSuccessLiveData?.observe(this,radioListResultObserver)
        radioPullRefreshViewModel?.rankListSuccessLiveData?.observe(this,radioRankListObserver)

        radioPullRefreshViewModel?.getXmlyRadios(Constants.RADIOTYPEPROVINCE,CITYCODEHENAN,Constants.DEFAULTPAGE)
        radioPullRefreshViewModel?.getRankRadioList()

    }


    public fun refreshWaveAnim(dataId:Long){
        (rvCity.adapter as RvHomeAdapter)?.selectDataId = dataId
        (rvRank.adapter as RvHomeAdapter)?.selectDataId = dataId
        (rvCity.adapter as RvHomeAdapter)?.notifyDataSetChanged()
        (rvRank.adapter as RvHomeAdapter)?.notifyDataSetChanged()

    }
}
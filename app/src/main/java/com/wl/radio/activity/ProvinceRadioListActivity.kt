package com.wl.radio.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.wl.radio.R
import com.wl.radio.adapter.ProvinceVpAdapter
import com.wl.radio.fragment.*
import com.wl.radio.util.Constants
import com.wl.radio.util.Constants.CITYCODE
import com.wl.radio.util.Constants.CITYNAME
import com.wl.radio.util.LogUtils
import com.wl.radio.viewmodel.RadioPullRefreshViewModel
import com.ximalaya.ting.android.opensdk.model.live.provinces.Province
import com.ximalaya.ting.android.opensdk.model.live.provinces.ProvinceList
import kotlinx.android.synthetic.main.activity_province_radio_list.*

class ProvinceRadioListActivity : BaseActivity() {
    var radioPullRefreshViewModel: RadioPullRefreshViewModel?=null
    val TAG="ProvinceRadioListActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_province_radio_list)

        initToolbar()


        //1.
        radioPullRefreshViewModel = ViewModelProviders.of(this).get(RadioPullRefreshViewModel::class.java)


        //2.
        lifecycle.addObserver(radioPullRefreshViewModel!!)

        //3.
       var provinceListObserver :Observer<ProvinceList>  =  Observer<ProvinceList> {

           LogUtils.d(TAG,it.provinceList.size.toString())

           initTablayout(it.provinceList)

       }


        var queryStatusObserver:Observer<String> = Observer {

            when(it){
                Constants.QUERYSTATUSLOADING -> showLoading()
                Constants.QUERYSTATUSFAILED -> showLoadFailed()
                Constants.QUERYSTATUSSUCCESS -> showLoadSuccess()
            }

        }


        //4.
        radioPullRefreshViewModel?.provinceListLiveData?.observe(this,provinceListObserver)
        radioPullRefreshViewModel?.queryStatusLiveData?.observe(this,queryStatusObserver)

        radioPullRefreshViewModel?.getProvinceList()

    }

    private fun initTablayout(provinceList: List<Province>) {
        var fragmentArrayList  : ArrayList<NormalRadioListFragment> = ArrayList<NormalRadioListFragment>()
        var cityNameList:ArrayList<String> = ArrayList();
        for (index in provinceList.indices) {
            LogUtils.d(TAG,"province"+provinceList.get(index).provinceName)
            var radioListFragment : NormalRadioListFragment = NormalRadioListFragment()
            var bundle:Bundle = Bundle()
            bundle.putString(CITYNAME,provinceList.get(index).provinceName)
            cityNameList.add(provinceList.get(index).provinceName)
            bundle.putLong(CITYCODE,provinceList.get(index).provinceCode)
            radioListFragment.arguments= bundle
            fragmentArrayList.add(radioListFragment)
        }

        LogUtils.d(TAG,"provinceSize"+fragmentArrayList.size)

        viewPager?.adapter = ProvinceVpAdapter(supportFragmentManager, fragmentArrayList,cityNameList)

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        tabLayout.setupWithViewPager(viewPager)





    }

    private fun initToolbar() {

        indexToolbar.setNavigationIcon(R.drawable.icon_back)

        indexToolbar.setTitleTextColor(Color.WHITE)
        indexToolbar.setTitle("")
        indexToolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                finish()
            }
        })


    }
}


package com.wl.radio.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wl.radio.fragment.NormalRadioListFragment
import com.wl.radio.util.LogUtils

class ProvinceVpAdapter : FragmentPagerAdapter{

    lateinit var vpFragments:ArrayList<NormalRadioListFragment>
    lateinit var cityNameList:ArrayList<String>
    val TAG="ProvinceVpAdapter"




    constructor(
        fm: FragmentManager?,
        vpFragments: ArrayList<NormalRadioListFragment>,
        cityNameList: ArrayList<String>
    ) : super(fm){
        this.vpFragments = vpFragments
        this.cityNameList = cityNameList
    }

//    override fun getPageTitle(position: Int): CharSequence? {
//        return "title"
//    }
//

    override fun getPageTitle(position: Int): CharSequence? {

        LogUtils.d(TAG,"pageTitles"+vpFragments?.get(position)?.cityName)
        return cityNameList?.get(position)
    }




    override fun getCount(): Int {
         return vpFragments.size
    }

    override fun getItem(position: Int): Fragment? {
        return vpFragments.get(position)
    }

}
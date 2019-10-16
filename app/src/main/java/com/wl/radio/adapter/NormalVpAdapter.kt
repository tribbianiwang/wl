package com.wl.radio.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wl.radio.fragment.NormalRadioListFragment
import com.wl.radio.util.LogUtils

class NormalVpAdapter : FragmentPagerAdapter{

    lateinit var vpFragments:ArrayList<Fragment>
    lateinit var cityNameList:ArrayList<String>




    constructor(
        fm: FragmentManager?,
        vpFragments: ArrayList<Fragment>,
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

        return cityNameList?.get(position)
    }




    override fun getCount(): Int {
         return vpFragments.size
    }

    override fun getItem(position: Int): Fragment? {
        return vpFragments.get(position)
    }

}
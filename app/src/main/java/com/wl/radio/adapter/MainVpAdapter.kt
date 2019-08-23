package com.wl.radio.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wl.radio.util.Constants.mainBottomTitles

class MainVpAdapter : FragmentPagerAdapter{

    lateinit var vpFragments:ArrayList<Fragment>





    constructor(fm: FragmentManager?,vpFragments:ArrayList<Fragment>) : super(fm){
        this.vpFragments = vpFragments
    }

//    override fun getPageTitle(position: Int): CharSequence? {
//        return "title"
//    }
//

    override fun getPageTitle(position: Int): CharSequence? {
        return "title"
    }




    override fun getCount(): Int {
         return vpFragments.size
    }

    override fun getItem(position: Int): Fragment? {
        return vpFragments.get(position)
    }

}
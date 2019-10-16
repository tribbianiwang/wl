package com.wl.radio.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.wl.radio.R
import com.wl.radio.adapter.NormalVpAdapter
import com.wl.radio.fragment.LookbackRadioFragment
import com.wl.radio.util.Constants
import com.wl.radio.util.Constants.LOOKBACKRADIOTITLES
import com.wl.radio.util.StringUtils
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import kotlinx.android.synthetic.main.activity_look_back_radio.*
import kotlinx.android.synthetic.main.activity_look_back_radio.tabLayout
import kotlinx.android.synthetic.main.activity_look_back_radio.viewPager
import kotlinx.android.synthetic.main.activity_province_radio_list.*
import kotlinx.android.synthetic.main.layout_defult_toolbar.*

class LookBackRadioActivity : BaseActivity() {

    lateinit var radio:Radio
    val fragmentList:ArrayList<Fragment> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_look_back_radio)
        radio = intent.getParcelableExtra(Constants.TRANSRADIO)
        index_toolbar.setNavigationOnClickListener { finish() }
        tv_title.text = radio.radioName+StringUtils.getString(R.string.string_lookback)



        for(i in Constants.LOOKBACKRADIOTITLES){
            var fragment: LookbackRadioFragment = LookbackRadioFragment()
            var bundle  = Bundle()
            bundle.putParcelable(Constants.TRANSRADIO,radio)
            bundle.putString(Constants.TRANS_LOOKBACK_TYPE,i)

            fragment.arguments = bundle

            fragmentList.add(fragment)
        }
        var lookBackVpAdapter = NormalVpAdapter(supportFragmentManager,fragmentList,
            LOOKBACKRADIOTITLES.toList() as ArrayList<String>
        )


        viewPager.adapter = lookBackVpAdapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        tabLayout.setupWithViewPager(viewPager)




    }
}

package com.wl.radio.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.wl.radio.R
import com.wl.radio.adapter.RvLookBackAdapter
import com.wl.radio.util.Constants.TRANSRADIO
import com.wl.radio.util.Constants.TRANS_LOOKBACK_TYPE
import com.wl.radio.util.RecyclerSpace
import com.wl.radio.util.StringUtils
import com.wl.radio.viewmodel.LookBackRadioViewModel
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.live.schedule.ScheduleList
import kotlinx.android.synthetic.main.fragment_lookback_radio.*

/**
 * A simple [Fragment] subclass.
 */
class LookbackRadioFragment : BaseFragment() {

    lateinit var radio:Radio
    lateinit var lookbackType:String
    lateinit var lookBackRadioViewModel: LookBackRadioViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lookback_radio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       arguments?.getParcelable<Radio>(TRANSRADIO)?.let {  radio =it }
      arguments?.getString(TRANS_LOOKBACK_TYPE)?.let {    lookbackType = it }
        rv_lookback_list.layoutManager = LinearLayoutManager(context)
//        tv.text = lookbackType+radio.radioName


        //1
        lookBackRadioViewModel=   ViewModelProviders.of(this).get(LookBackRadioViewModel::class.java)

        //2
        lifecycle.addObserver(lookBackRadioViewModel)

        //3
        var lookBackBeansObserver:Observer<ScheduleList> = Observer {
            var rvLookBackAdapter = RvLookBackAdapter(it.getmScheduleList(),lookbackType)
            rv_lookback_list.adapter = rvLookBackAdapter

        }


        //4
        lookBackRadioViewModel.errorMsgLiveData?.observe(this,errorMsgObserver)
        lookBackRadioViewModel.queryStatusLiveData?.observe(this,queryStatusObserver)
        lookBackRadioViewModel.lookBackProgramsLiveData.observe(this,lookBackBeansObserver)

        lookBackRadioViewModel.getLookBackPrograms(radio.dataId.toString(),getDayOfWeek())
    }


    fun getDayOfWeek():String{
        when(lookbackType){
            "昨天"->     return StringUtils.getWeekOfDate(StringUtils.getOldDate(-1))
            "今天"->     return StringUtils.getWeekOfDate(StringUtils.getOldDate(0))
            "明天"->     return StringUtils.getWeekOfDate(StringUtils.getOldDate(1))
        }
        return "0"
    }


}

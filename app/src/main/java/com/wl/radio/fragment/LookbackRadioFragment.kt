package com.wl.radio.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wl.radio.R
import com.wl.radio.util.Constants.TRANSRADIO
import com.wl.radio.util.Constants.TRANS_LOOKBACK_TYPE
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import kotlinx.android.synthetic.main.fragment_lookback_radio.*

/**
 * A simple [Fragment] subclass.
 */
class LookbackRadioFragment : Fragment() {

    lateinit var radio:Radio
    lateinit var lookbackType:String
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

        tv_name.text = lookbackType+radio.radioName
    }


}

package com.wl.radio.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wl.radio.R
import com.wl.radio.adapter.RvHomeAdapter
import kotlinx.android.synthetic.main.layout_fragment_collection.view.*

class CollectingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView:View =View.inflate(context, R.layout.layout_fragment_collection,null)
        contentView.rvCollection.layoutManager=LinearLayoutManager(context)
//        contentView.rvCollection.adapter=(RvHomeAdapter(context, recordList))


       return contentView

    }
}
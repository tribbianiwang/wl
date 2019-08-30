package com.wl.radio.adapter

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wl.radio.R
import com.wl.radio.adapter.RvCollectAdapter.ViewHolder
import com.wl.radio.util.ImgUtils
import com.wl.radio.util.LogUtils
import com.wl.radio.util.RvItemClickListener
import com.wl.radio.util.RvItemLongClickListener
import com.wl.radio.util.StringUtils.formatPlayCount
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import kotlinx.android.synthetic.main.layout_rv_collect_radio.view.*
import kotlinx.android.synthetic.main.layout_rv_item_home_radio.view.*
import kotlinx.android.synthetic.main.layout_rv_item_home_radio.view.ivPlayWave
import kotlinx.android.synthetic.main.layout_rv_item_home_radio.view.ivRadioLogo
import kotlinx.android.synthetic.main.layout_rv_item_home_radio.view.tvPeopleTimes
import kotlinx.android.synthetic.main.layout_rv_item_home_radio.view.tvRadioName
import kotlinx.android.synthetic.main.layout_rv_item_home_radio.view.tvShowName

class RvCollectAdapter: RecyclerView.Adapter<ViewHolder>{


    val TAG="RvHomeAdapter"

    var recordList:MutableList<Radio>?=null


    var selectDataId:Long =0

     var mItemClickListener: RvItemClickListener? = null

    interface OnDeleteItemListener{
        fun deleteItem(position:Int)
    }

    var onDeleteItemListener:OnDeleteItemListener?=null


    constructor(
        recordList: MutableList<Radio>?

    ) : super() {
        this.recordList = recordList;

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_rv_collect_radio, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
    return recordList?.size!!
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tvRadioName.text=recordList?.get(position)?.radioName
        holder.itemView.tvShowName.text=recordList?.get(position)?.programName
        holder.itemView.tvPeopleTimes.text= recordList?.get(position)?.radioPlayCount?.toLong()?.let {
            formatPlayCount(
                it
            )
        }
        recordList?.get(position)?.coverUrlLarge?.let { ImgUtils.showImage(holder.itemView.context, it,holder.itemView.ivRadioLogo) }




        if(recordList?.get(position)?.dataId?.equals(selectDataId)?:false){
            holder.itemView.ivPlayWave.visibility = View.VISIBLE
            var waveAnim: AnimationDrawable = holder.itemView.ivPlayWave.background as AnimationDrawable
            waveAnim.start()
        }else{
            holder.itemView.ivPlayWave.visibility = View.GONE
        }


        holder.itemView.ll_content.setOnClickListener {
            mItemClickListener?.onItemClick(it,position)

        }

        holder.itemView.bt_delete.setOnClickListener {
            onDeleteItemListener?.deleteItem(position)
        }



    }


    inner class ViewHolder(
        itemView: View

    ) : RecyclerView.ViewHolder(itemView){







    }


}
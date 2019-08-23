package com.wl.radio.adapter

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wl.radio.R
import com.wl.radio.adapter.RvHomeAdapter.ViewHolder
import com.wl.radio.util.ImgUtils
import com.wl.radio.util.LogUtils
import com.wl.radio.util.RvItemClickListener
import com.wl.radio.util.RvItemLongClickListener
import com.wl.radio.util.StringUtils.formatPlayCount
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import kotlinx.android.synthetic.main.layout_rv_item_home_radio.view.*

class RvHomeAdapter: RecyclerView.Adapter<ViewHolder>{


    lateinit var context: Context
    val TAG="RvHomeAdapter"

    var recordList:MutableList<Radio>?=null


    var selectDataId:Long =0

    private var mItemLongListener: RvItemLongClickListener? = null
    private var mItemClickListener: RvItemClickListener? = null
    constructor(
        context: Context?,
        recordList: MutableList<Radio>?

    ) : super() {
        this.context = context!!
        this.recordList = recordList;

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_rv_item_home_radio, parent, false)
        return ViewHolder(view, mItemClickListener, mItemLongListener)
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
        recordList?.get(position)?.coverUrlLarge?.let { ImgUtils.showImage(context, it,holder.itemView.ivRadioLogo) }




        if(recordList?.get(position)?.dataId?.equals(selectDataId)?:false){
            holder.itemView.ivPlayWave.visibility = View.VISIBLE
            var waveAnim: AnimationDrawable = holder.itemView.ivPlayWave.background as AnimationDrawable
            waveAnim.start()
        }else{
            holder.itemView.ivPlayWave.visibility = View.GONE
        }



    }


    inner class ViewHolder(
        itemView: View,
        private val mListener: RvItemClickListener?,
        private val mLongListener: RvItemLongClickListener?
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {



        init {

            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)


        }

        override fun onClick(v: View) {
            mListener?.onItemClick(v, adapterPosition)
        }

        override fun onLongClick(v: View): Boolean {
            mLongListener?.onItemLongClick(v, adapterPosition)
            return true
        }

    }


    fun setOnItemClickListener(listener: RvItemClickListener) {
        this.mItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: RvItemLongClickListener) {
        this.mItemLongListener = listener
    }

}
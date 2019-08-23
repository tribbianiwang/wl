package com.wl.radio.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wl.radio.R
import com.wl.radio.adapter.RvNaviAdapter.ViewHolder
import com.wl.radio.util.Constants
import com.wl.radio.util.Constants.NAVIIMGS
import com.wl.radio.util.Constants.NAVITITLES
import com.wl.radio.util.RvItemClickListener
import com.wl.radio.util.RvItemLongClickListener
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioCategory
import kotlinx.android.synthetic.main.rv_item_navi.view.*

class RvNaviAdapter : RecyclerView.Adapter<ViewHolder> {

    var radioCategories: MutableList<RadioCategory>? = null
    lateinit var context: Context
    private var mItemClickListener: RvItemClickListener? = null
    private var mItemLongListener: RvItemLongClickListener? = null
    private var iconTitle =mutableMapOf<String, Int>()

    constructor(
        context: Context?,
        radioCategories: MutableList<RadioCategory>
    ) : super() {
        this.context = context!!
        this.radioCategories = radioCategories

        for (index in NAVIIMGS.indices) {
            iconTitle?.put(NAVITITLES[index],NAVIIMGS[index])
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_item_navi, parent, false)
        return ViewHolder(view, mItemClickListener, mItemLongListener)
    }

    override fun getItemCount(): Int {
        return radioCategories?.size?:0;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tvTitle.text = radioCategories?.get(position)?.radioCategoryName
        var imgId=iconTitle.get(radioCategories?.get(position)?.radioCategoryName)

            if (imgId != null) {
                holder.itemView.ivTop.setImageResource(imgId)
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
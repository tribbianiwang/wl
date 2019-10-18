package com.wl.radio.adapter

import android.app.Activity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wl.radio.R
import com.wl.radio.util.RvItemClickListener
import com.wl.radio.util.StringUtils
import com.wl.radio.util.T
import com.ximalaya.ting.android.opensdk.model.live.schedule.LiveAnnouncer
import com.ximalaya.ting.android.opensdk.model.live.schedule.Schedule
import kotlinx.android.synthetic.main.layout_rv_lookback_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class RvLookBackAdapter(internal var mScheduleList: List<Schedule>, lookbackType: String) :
    RecyclerView.Adapter<RvLookBackAdapter.ViewHolder>() {
    var lookbackType: String

    var mItemClickListener: RvItemClickListener? = null

    init {
        this.lookbackType = lookbackType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var contentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_rv_lookback_item, parent, false)
        return ViewHolder(contentView)

    }

    override fun getItemCount(): Int {

        return mScheduleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.itemView.tv_program_name.text =
            mScheduleList.get(position).relatedProgram.programName
        holder.itemView.tv_host_names.text =
            getAnnouncersString(mScheduleList.get(position).relatedProgram.announcerList)
        holder.itemView.tv_start_end_time.text =
            mScheduleList.get(position).startTime + "-" + mScheduleList.get(position).endTime

        when(lookbackType){
            "昨天"->{
                holder.itemView.tv_look_back_status.text = StringUtils.getString(R.string.string_listen_back)
                holder.itemView.tv_look_back_status.setTextColor(StringUtils.getColor(R.color.design_text_alpha_gray))
            }
                "今天"->   {
                if(TextUtils.isEmpty(mScheduleList.get(position).listenBackUrl)){
                    if(position>1&&!TextUtils.isEmpty(mScheduleList.get(position-1).listenBackUrl)){
                        holder.itemView.tv_look_back_status.text = StringUtils.getString(R.string.string_live)
                        holder.itemView.tv_look_back_status.setTextColor(StringUtils.getColor(R.color.colorPrimary))
                    }else{
                        holder.itemView.tv_look_back_status.text = StringUtils.getString(R.string.string_not_begin)
                        holder.itemView.tv_look_back_status.setTextColor(StringUtils.getColor(R.color.design_text_alpha_gray))
                    }

                }else{
                    holder.itemView.tv_look_back_status.setTextColor(StringUtils.getColor(R.color.design_text_alpha_gray))
                    holder.itemView.tv_look_back_status.text = StringUtils.getString(R.string.string_listen_back)
                }
            }
            "明天"->    holder.itemView.tv_look_back_status.text = StringUtils.getString(R.string.string_not_begin)
        }

            holder.itemView.setOnClickListener {

                if(holder.itemView.tv_look_back_status.text.equals(StringUtils.getString(R.string.string_live))){
                    (holder.itemView.context as Activity).finish()
                }else if(holder.itemView.tv_look_back_status.text.equals(StringUtils.getString(R.string.string_not_begin))){
                    T.showShort(holder.itemView.context,mScheduleList.get(position).relatedProgram.programName+StringUtils.getString(R.string.program_not_start_yet))
                }else{
                    mItemClickListener?.onItemClick(it,position)
                }
            }
    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


    fun getAnnouncersString(announcerList: List<LiveAnnouncer>): String {
        var announcersString: StringBuffer = StringBuffer()
        for (announcer in announcerList) {
            announcersString.append(announcer.nickName + "  ")
        }

        return if (TextUtils.isEmpty(announcersString.toString())) StringUtils.getString(R.string.string_unknow_announcer) else announcersString.toString()
    }




}
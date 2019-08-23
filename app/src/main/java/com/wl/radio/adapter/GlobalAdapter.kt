package com.wl.radio.adapter

import android.view.View
import com.billy.android.loading.Gloading
import com.wl.radio.util.Constants
import com.wl.radio.view.GlobalLoadingStatusView

class GlobalAdapter : Gloading.Adapter {

    override fun getView(holder: Gloading.Holder, convertView: View?, status: Int): View {
        var loadingStatusView: GlobalLoadingStatusView? = null
        //reuse the old view, if possible
        if (convertView != null && convertView is GlobalLoadingStatusView) {
            loadingStatusView = convertView as GlobalLoadingStatusView?
        }
        if (loadingStatusView == null) {
            loadingStatusView = GlobalLoadingStatusView(holder.context, holder.retryTask)
        }
        loadingStatusView!!.setStatus(status)
        val data = holder.getData<Any>()
        //show or not show msg view
        val hideMsgView = Constants.HIDE_LOADING_STATUS_MSG.equals(data)
        loadingStatusView!!.setMsgViewVisibility(!hideMsgView)
        return loadingStatusView
    }

}
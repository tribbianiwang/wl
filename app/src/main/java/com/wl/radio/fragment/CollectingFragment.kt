package com.wl.radio.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wl.radio.MyApplication
import com.wl.radio.activity.PlayingActivity
import com.wl.radio.adapter.RvCollectAdapter
import com.wl.radio.bean.CollectRadioBean
import com.wl.radio.util.Constants
import com.wl.radio.util.LogUtils
import com.wl.radio.util.RvItemClickListener
import com.wl.radio.util.RvItemLongClickListener
import com.wl.radio.viewmodel.CollectRadioViewModel
import com.wl.radio.viewmodel.RadioLiveViewModel
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import kotlinx.android.synthetic.main.layout_fragment_collection.view.*
import java.util.*


class CollectingFragment : BaseFragment() {
    lateinit var collectRadioViewModel: CollectRadioViewModel
    lateinit var radioLiveViewModel: RadioLiveViewModel
    var collectRadios:MutableList<Radio>?=null
    var collectRadioBeans:MutableList<CollectRadioBean>?=null
    var rvCollectAdapter: RvCollectAdapter?=null
    var deletePosition = -5
    var allCollectRadioObserver: Observer<List<CollectRadioBean>>?=null
    var radioListByIdsObserver :Observer<List<Radio>>?=null
    var queryStatusChildObserver: Observer<String>?=null
    val TAG = "CollectingFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        val contentView: View =
            View.inflate(context, com.wl.radio.R.layout.layout_fragment_collection, null)
        contentView.rvCollection.layoutManager = LinearLayoutManager(context)
        val initLoadingStatusView = initLoadingStatusView(contentView)



//        contentView.rvCollection.adapter=(RvHomeAdapter(context, recordList))
        //1
        collectRadioViewModel = ViewModelProviders.of(this).get(CollectRadioViewModel::class.java)
        radioLiveViewModel = ViewModelProviders.of(this).get(RadioLiveViewModel::class.java)



        //2
        lifecycle.addObserver(collectRadioViewModel)
        lifecycle.addObserver(radioLiveViewModel)

        //3




        if(allCollectRadioObserver==null){
            allCollectRadioObserver= Observer {
                collectRadioBeans = it as MutableList<CollectRadioBean>?
                var sbRadioIds = StringBuffer()
                for (i in it.indices) {
                    if (i == (it.size - 1)) {
                        sbRadioIds.append(it[i].radioId)
                    } else {
                        sbRadioIds.append(it[i].radioId + ",")
                    }

                }
                if(sbRadioIds==null||sbRadioIds.length==0){
                    showEmpty()
                }else{
                    radioLiveViewModel.getRadioInfos(sbRadioIds.toString())
                }




            }

        }

        if(radioListByIdsObserver==null){
            radioListByIdsObserver =   object : Observer<List<Radio>> {
                override fun onChanged(radioList: List<Radio>) {
                    collectRadios=radioList as MutableList<Radio>

                    rvCollectAdapter = RvCollectAdapter(collectRadios as MutableList<Radio>)
                    contentView.rvCollection.adapter = rvCollectAdapter
                    rvCollectAdapter?.mItemClickListener = object:RvItemClickListener{
                        override fun onItemClick(view: View, position: Int) {

                            MyApplication.refreshRadioList(collectRadios as ArrayList)
                            var intent: Intent = Intent(context, PlayingActivity::class.java)
                            rvCollectAdapter?.selectDataId = collectRadios?.get(position)?.dataId ?: 0
                            rvCollectAdapter?.notifyDataSetChanged()
                            intent.putExtra(Constants.TRANSRADIO, collectRadios?.get(position))


                            startActivity(intent)

                        }

                    }

                    rvCollectAdapter?.onDeleteItemListener = object :RvCollectAdapter.OnDeleteItemListener{
                        override fun deleteItem(position: Int) {
                            deletePosition = position
                            if(collectRadioViewModel!=null){
                                collectRadioBeans?.get(position)?.let {
                                    collectRadioViewModel.deleteCollectRadio(
                                        it
                                    )
                                }
                            }

                        }

                    }
                }
        }


        }

        if(queryStatusChildObserver==null){
            queryStatusChildObserver= Observer { status ->
                when (status) {
                    Constants.QUERYSTATUSLOADING -> showProgress(activity)

                    Constants.QUERYSTATUSSUCCESS -> hideProgress()

                    Constants.QUERYSTATUSFAILED -> hideProgress()

                    Constants.QUERYDELETESUCCESS->{
                        hideProgress()
                        collectRadioBeans?.removeAt(deletePosition)
                        collectRadios?.removeAt(deletePosition)
                        rvCollectAdapter?.notifyItemRemoved(deletePosition)
                        rvCollectAdapter?. notifyItemRangeChanged(deletePosition, collectRadioBeans?.size?:0 - deletePosition);

                        if(collectRadioBeans?.size==0||collectRadios?.size==0){
                            showEmpty()
                        }
                    }
                }
            }

        }



        //4
        collectRadioViewModel.allCollectRadioLiveData.observe(this, allCollectRadioObserver!!)
        collectRadioViewModel.queryStatusLiveData?.observe(this, queryStatusChildObserver!!)
        collectRadioViewModel.errorMsgLiveData?.observe(this, errorMsgObserver)

        radioLiveViewModel.queryStatusLiveData?.observe(this, gLoadingqueryStatusObserver)
        radioLiveViewModel.errorMsgLiveData?.observe(this, errorMsgObserver)
        radioLiveViewModel.radiolistFromIds?.observe(this, radioListByIdsObserver!!)


        collectRadioViewModel.queryAllCollectRadio()





        return initLoadingStatusView?.wrapper

    }



    override fun onLoadRetry() {
       if(collectRadioViewModel!=null){
           collectRadioViewModel.queryAllCollectRadio()
       }
    }


}
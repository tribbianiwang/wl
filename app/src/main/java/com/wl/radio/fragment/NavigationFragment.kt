package com.wl.radio.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.wl.radio.R
import com.wl.radio.activity.NormalRadioListActivity
import com.wl.radio.adapter.RvNaviAdapter
import com.wl.radio.util.*
import com.wl.radio.util.Constants.ISQUERYCATEGORY
import com.wl.radio.util.Constants.RADIOCATEGORYID
import com.wl.radio.viewmodel.RadioPullRefreshViewModel
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioCategoryList
import kotlinx.android.synthetic.main.layout_fragment_navigation.*
import kotlinx.android.synthetic.main.layout_fragment_navigation.view.*

class NavigationFragment : Fragment() {
    var radioPullRefreshViewModel: RadioPullRefreshViewModel?=null
    val TAG="NavigationFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var contentView = View.inflate(context, R.layout.layout_fragment_navigation,null)
        contentView.rvNavi.layoutManager = GridLayoutManager(context,4);

       return contentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //1.
        radioPullRefreshViewModel = ViewModelProviders.of(this).get(RadioPullRefreshViewModel::class.java)

        //2
        lifecycle.addObserver(radioPullRefreshViewModel!!)

        //3
        val queryStatusObserver: Observer<String> = Observer {

            when(it){
//                Constants.QUERYSTATUSLOADING -> showLoading()
//                Constants.QUERYSTATUSFAILED -> showLoadFailed()
//                Constants.QUERYSTATUSSUCCESS -> showLoadSuccess()
            }

        }

        val categoryObersver:Observer<RadioCategoryList> = Observer {
            var rvNaviAdapter= RvNaviAdapter(context,it.radioCategories)
            rvNavi.adapter = rvNaviAdapter
            rvNavi.addItemDecoration(RecyclerSpace(1,StringUtils.getRColor(context,R.color.gray_white)))

            rvNaviAdapter?.setOnItemClickListener(object :RvItemClickListener{
                override fun onItemClick(view: View, position: Int) {
                 LogUtils.d(TAG,"radioCategory:"+it.radioCategories?.get(position)?.radioCategoryName)
                var intent: Intent =Intent(activity,NormalRadioListActivity::class.java)

//                    isQueryCategory = intent.getBooleanExtra(Constants.ISQUERYCATEGORY,false)
//                    radioCategoryId = intent.getIntExtra(Constants.RADIOCATEGORYID,0)
                    intent.putExtra(ISQUERYCATEGORY,true)
                    intent.putExtra(RADIOCATEGORYID,it.radioCategories?.get(position)?.id)
                    startActivity(intent)


                }

            })






        }


        //4.
        radioPullRefreshViewModel?.queryStatusLiveData?.observe(this,queryStatusObserver)
        radioPullRefreshViewModel?.radioCategoryListLiveData?.observe(this,categoryObersver)

        radioPullRefreshViewModel?.getRadioCategoriesList()

    }


}
package com.wl.radio.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wl.radio.MyApplication
import com.wl.radio.R
import com.wl.radio.util.Constants.QUERYSTATUSFAILED
import com.wl.radio.util.Constants.QUERYSTATUSLOADING
import com.wl.radio.util.Constants.QUERYSTATUSSUCCESS
import com.wl.radio.util.Constants.TRANSRADIO
import com.wl.radio.util.ImgUtils
import com.wl.radio.util.LogUtils
import com.wl.radio.viewmodel.RadioLiveViewModel
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest.getRadios
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.live.program.ProgramList
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.appnotification.NotificationColorUtils
import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException

import kotlinx.android.synthetic.main.activity_playing.*





class PlayingActivity : BaseActivity() , IXmPlayerStatusListener {


    val TAG:String="PlayingActivity"
    var mPlayerManager: XmPlayerManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playing)
        initToolbar()

        mPlayerManager = XmPlayerManager.getInstance(this)
        val mNotification = XmNotificationCreater.getInstanse(this)
            .initNotification(this.applicationContext, PlayingActivity::class.java)
        NotificationColorUtils.isTargerSDKVersion24More = true;
        mPlayerManager?.init(System.currentTimeMillis().toInt(), mNotification)
        mPlayerManager?.addPlayerStatusListener(this)

        val transRadio = intent.getParcelableExtra<Radio>(TRANSRADIO);


        //1
        val radioLiveViewModel = ViewModelProviders.of(this).get(RadioLiveViewModel::class.java)

        //2
        lifecycle.addObserver(radioLiveViewModel)

        //3
        val queryStatusObserver:Observer<String> = Observer {

            when(it){
                QUERYSTATUSLOADING->LogUtils.d(TAG,QUERYSTATUSLOADING)
                QUERYSTATUSFAILED->LogUtils.d(TAG,QUERYSTATUSFAILED)
                QUERYSTATUSSUCCESS->LogUtils.d(TAG,QUERYSTATUSSUCCESS)
            }

        }

        val errorMsgObserver:Observer<String> = Observer{
            LogUtils.d(TAG,it)
        }

        val radioListResultObserver:Observer<RadioList> = Observer{


            val selectRadio = it.radios.get(0)

            setViewData(selectRadio,false)



        }


        val radioInfoObserver:Observer<ProgramList> = Observer {
            val getmProgramList = it?.getmProgramList();
            if (getmProgramList != null) {
                for(index in getmProgramList.indices){
                    LogUtils.d(TAG,"radioInfo:"+getmProgramList?.get(index))
                     var selectRadio:Radio = Radio()
                    selectRadio.coverUrlLarge=getmProgramList?.get(index).backPicUrl
                    selectRadio.radioName=getmProgramList?.get(index).programName
                    selectRadio.programName=getmProgramList?.get(index).programName
                    setViewData(selectRadio,true)

                }

            }

        }

        //4

        radioLiveViewModel.errorMsgLiveData?.observe(this,errorMsgObserver)
        radioLiveViewModel.queryStatusLiveData?.observe(this,queryStatusObserver)
        radioLiveViewModel.radioListLiveData?.observe(this,radioListResultObserver)
        radioLiveViewModel.radioInfoLiveData?.observe(this,radioInfoObserver)

        if(transRadio!=null){
            //从列表中点击进入
            setViewData(transRadio,false)
            LogUtils.d(TAG,"transRadio!=null")

        }else{
            if(!(mPlayerManager?.isPlaying?:false)){
                //没有正在播放的广播
                LogUtils.d(TAG,"transRadio==null")
                radioLiveViewModel.getXmlyRadios(1,0,1)
            }else{
                //有正在播放的
                mPlayerManager?.getCurrSound()?.dataId?.let { radioLiveViewModel?.getRadioInfo(it) }
            }

        }





    }

    private fun setTitleAndImg(selectRadio: Radio?){
        selectRadio?.coverUrlLarge?.let { ImgUtils.showImage(this, it,ivCover) }
        tvRadioName.text= selectRadio?.programName
        tvTitle.text=selectRadio?.radioName
    }

    private fun setViewData(selectRadio: Radio?,isOnlySetView:Boolean) {
        if(!isOnlySetView){
            mPlayerManager?.playActivityRadio(selectRadio)

        }

        setTitleAndImg(selectRadio)


        ivPlayPause.setOnClickListener{
            if(mPlayerManager?.isPlaying!!){

                mPlayerManager!!.pause()
            }else{
                mPlayerManager?.play()
            }

        }
        ivPlayNext.setOnClickListener{

            setTitleAndImg( MyApplication.playNextRadio())

        }

        ivPlayPrevious.setOnClickListener{
            setTitleAndImg(MyApplication.playPreRadio())
        }


    }


    override fun onPlayStart() {
        LogUtils.d(TAG,"onPlayStart")

        ivPlayPause.setImageResource(R.drawable.selector_pause_drawable)
    }

    override fun onSoundSwitch(p0: PlayableModel?, p1: PlayableModel?) {
    }

    override fun onPlayProgress(p0: Int, p1: Int) {
        ivPlayPause.setImageResource(R.drawable.selector_pause_drawable)
//        when(mPlayerManager?.isAdPlaying){
//            false->tvAd.text="节目播放中"
//            true->tvAd.text="广告播放中"
//        }
        LogUtils.d(TAG,"onPlayProgress"+mPlayerManager?.isAdPlaying)

    }

    override fun onPlayPause() {
        LogUtils.d(TAG,"onPlayPause")
        ivPlayPause.setImageResource(R.drawable.selector_play_drawable)
    }

    override fun onBufferProgress(p0: Int) {
    }

    override fun onPlayStop() {
        ivPlayPause.setImageResource(R.drawable.selector_play_drawable)
        LogUtils.d(TAG,"onPlayStop")
    }

    override fun onBufferingStart() {
    }

    override fun onSoundPlayComplete() {
        LogUtils.d(TAG,"onPlayStop")
    }

    override fun onError(p0: XmPlayerException?): Boolean {
        ivPlayPause.setImageResource(R.drawable.selector_play_drawable)
        return false
    }

    override fun onSoundPrepared() {
    }

    override fun onBufferingStop() {
    }




    private fun initToolbar() {

        indexToolbar.setNavigationIcon(R.drawable.icon_back)

        indexToolbar.setTitleTextColor(Color.WHITE)
        indexToolbar.setTitle("")
        indexToolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                finish()
            }
        })


    }
}

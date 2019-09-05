package com.wl.radio.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wl.radio.MyApplication
import com.wl.radio.R
import com.wl.radio.bean.CollectRadioBean
import com.wl.radio.util.Constants
import com.wl.radio.util.Constants.BROADCAST_REFRESH_PLAY_RADIO_HISTORY
import com.wl.radio.util.Constants.QUERYSTATUSFAILED
import com.wl.radio.util.Constants.QUERYSTATUSLOADING
import com.wl.radio.util.Constants.QUERYSTATUSSUCCESS
import com.wl.radio.util.Constants.RESET_RADIO_IMG_AND_TITLE_ACTION
import com.wl.radio.util.Constants.TRANSRADIO
import com.wl.radio.util.Constants.TRANS_PLAYING_RADIO
import com.wl.radio.util.ImgUtils
import com.wl.radio.util.LogUtils
import com.wl.radio.util.StringUtils
import com.wl.radio.viewmodel.CollectRadioViewModel
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


class PlayingActivity : BaseActivity(), IXmPlayerStatusListener ,
android.view.GestureDetector.OnGestureListener {


    val TAG: String = "PlayingActivity"
    var mPlayerManager: XmPlayerManager? = null
	// 定义手势检测器实例
    lateinit var detector: GestureDetector
    var animator: ObjectAnimator? = null

     var playingRadio:Radio?=null
    lateinit var  collectRadioViewModel:CollectRadioViewModel


    var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                RESET_RADIO_IMG_AND_TITLE_ACTION -> {

                    setTitleAndImg(intent.getParcelableExtra<Radio>(TRANSRADIO))
                }
            }

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playing)
        initToolbar()

		// 创建手势检测器
		detector =  GestureDetector(this ,this);

        mPlayerManager = XmPlayerManager.getInstance(this)
        val mNotification = XmNotificationCreater.getInstanse(this)
            .initNotification(this.applicationContext, PlayingActivity::class.java)
        NotificationColorUtils.isTargerSDKVersion24More = true;
        mPlayerManager?.init(System.currentTimeMillis().toInt(), mNotification)
        mPlayerManager?.addPlayerStatusListener(this)

        val transRadio = intent.getParcelableExtra<Radio>(TRANSRADIO);


        //1
        val radioLiveViewModel = ViewModelProviders.of(this).get(RadioLiveViewModel::class.java)
        collectRadioViewModel=  ViewModelProviders.of(this).get(CollectRadioViewModel::class.java)


        //2
        lifecycle.addObserver(radioLiveViewModel)
        lifecycle.addObserver(collectRadioViewModel)

        //3
        val queryStatusObserver: Observer<String> = Observer {

            when (it) {
                QUERYSTATUSLOADING -> LogUtils.d(TAG, QUERYSTATUSLOADING)
                QUERYSTATUSFAILED -> LogUtils.d(TAG, QUERYSTATUSFAILED)
                QUERYSTATUSSUCCESS -> LogUtils.d(TAG, QUERYSTATUSSUCCESS)
            }

        }

        val errorMsgObserver: Observer<String> = Observer {
            LogUtils.d(TAG, it)
        }

        val radioListResultObserver: Observer<RadioList> = Observer {


            val selectRadio = it.radios.get(0)

            setViewData(selectRadio, false)


        }


        val radioInfoObserver: Observer<ProgramList> = Observer {
            val getmProgramList = it?.getmProgramList();
            if (getmProgramList != null) {
                for (index in getmProgramList.indices) {
                    LogUtils.d(TAG, "radioInfo:" + getmProgramList?.get(index))
                    var selectRadio: Radio = Radio()
                    selectRadio.coverUrlLarge = getmProgramList?.get(index).backPicUrl
                    selectRadio.radioName = getmProgramList?.get(index).programName
                    selectRadio.programName = getmProgramList?.get(index).programName
                    setViewData(selectRadio, true)

                }

            }

        }



        //4

        radioLiveViewModel.errorMsgLiveData?.observe(this, errorMsgObserver)
        radioLiveViewModel.queryStatusLiveData?.observe(this, queryStatusObserver)
        radioLiveViewModel.radioListLiveData?.observe(this, radioListResultObserver)
        radioLiveViewModel.radioInfoLiveData?.observe(this, radioInfoObserver)




        if (transRadio != null) {
            //从列表中点击进入
            setViewData(transRadio, false)
            LogUtils.d(TAG, "transRadio!=null")

        } else {
            if (!(mPlayerManager?.isPlaying ?: false)) {
                //没有正在播放的广播
                radioLiveViewModel.getXmlyRadios(1, 0, 1)
            } else {
                //有正在播放的
                mPlayerManager?.getCurrSound()?.dataId?.let { radioLiveViewModel?.getRadioInfo(it) }
            }

        }


        var intentFilter = IntentFilter(RESET_RADIO_IMG_AND_TITLE_ACTION)
        registerReceiver(broadcastReceiver, intentFilter)

        initAnim()

    }

    private fun setTitleAndImg(selectRadio: Radio?) {
        playingRadio = selectRadio
        playingRadio?.let { MyApplication.addHistoryRadios(it)

            var intent  = Intent()
            intent.setAction(BROADCAST_REFRESH_PLAY_RADIO_HISTORY)
            intent.putExtra(TRANS_PLAYING_RADIO,selectRadio)
            this@PlayingActivity.sendBroadcast(intent)

        }
        selectRadio?.coverUrlLarge?.let { ImgUtils.showImage(this, it, ivCover) }
        tvRadioName.text = selectRadio?.programName
        tvTitle.text = selectRadio?.radioName
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    private fun setViewData(selectRadio: Radio?, isOnlySetView: Boolean) {

        if (!isOnlySetView) {
            mPlayerManager?.playActivityRadio(selectRadio)

        }

        setTitleAndImg(selectRadio)


        ivPlayPause.setOnClickListener {
            if (mPlayerManager?.isPlaying!!) {

                mPlayerManager!!.pause()
            } else {
                mPlayerManager?.play()
            }

        }
        ivPlayNext.setOnClickListener {

            MyApplication.playNextRadio()

        }

        ivPlayPrevious.setOnClickListener {
            MyApplication.playPreRadio()
        }



        ivCover.setOnClickListener{
            LogUtils.d(TAG,"collectRadio:queryAll")
            collectRadioViewModel.queryAllCollectRadio()
        }

        iv_collect_radio.setOnClickListener{
            if(iv_collect_radio.getTag().equals(StringUtils.getString(R.string.unselected))){
                //开始执行收藏
                iv_collect_radio.setImageResource(R.drawable.icon_heart_red)
                iv_collect_radio.setTag(StringUtils.getString(R.string.selected))
                //收藏电台id
                LogUtils.d(TAG,"radioName:"+playingRadio?.radioName+playingRadio?.dataId)
                collectRadioViewModel.addCollectRadio(playingRadio?.dataId.toString())

            }else{
                //开始执行取消收藏
                iv_collect_radio.setImageResource(R.drawable.icon_heart_gray)
                iv_collect_radio.setTag(StringUtils.getString(R.string.unselected))

            }

        }


    }


    override fun onPlayStart() {
        LogUtils.d(TAG, "onPlayStart")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            startRotateAnim()
        }
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
        LogUtils.d(TAG, "onPlayProgress" + mPlayerManager?.isAdPlaying)

    }

    override fun onPlayPause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            stopRotateAnim()
        }
        ivPlayPause.setImageResource(R.drawable.selector_play_drawable)
    }

    override fun onBufferProgress(p0: Int) {
    }

    override fun onPlayStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            stopRotateAnim()
        }
        ivPlayPause.setImageResource(R.drawable.selector_play_drawable)

    }

    override fun onBufferingStart() {
    }

    override fun onSoundPlayComplete() {
        LogUtils.d(TAG, "onPlayStop")
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

    var isAnimRunning: Boolean = false
    fun initAnim() {
        animator = ObjectAnimator.ofFloat(ivCover, "rotation", 0f, 360.0f);
        animator?.setDuration(20000);
        animator?.setInterpolator(LinearInterpolator());//不停顿
        animator?.setRepeatCount(-1);//设置动画重复次数
        animator?.setRepeatMode(ValueAnimator.RESTART);//动画重复模式
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun startRotateAnim() {

        if (isAnimRunning) {
            animator?.resume();
        } else {
            animator?.start();//开始动画
            isAnimRunning = true;
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun stopRotateAnim() {

        animator?.pause();
    }



    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return detector.onTouchEvent(event)
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {


        val minMove = 120f // 最小滑动距离
        val minVelocity = 0f // 最小滑动速度
        val beginX = e1?.x?:0f
        val endX = e2?.x?:0f
        val beginY = e1?.y?:0f
        val endY = e2?.y?:0f

        if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity) { // 左滑
            MyApplication.playNextRadio()
        } else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity) { // 右滑
            MyApplication.playPreRadio()
        } else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) { // 上滑
        } else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) { // 下滑
        }

        return false

    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {

    }

}

package com.wl.radio.activity


import android.os.Bundle
import androidx.fragment.app.Fragment
import com.wl.radio.adapter.MainVpAdapter
import com.wl.radio.fragment.*
import kotlinx.android.synthetic.main.activity_main.*

import android.view.LayoutInflater
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.wl.radio.R
import com.wl.radio.util.Constants.mainBottomTitles
import com.wl.radio.util.LogUtils
import kotlinx.android.synthetic.main.layout_tab_item.view.*
import com.yanzhenjie.permission.AndPermission
import android.Manifest.permission
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.*

import com.yanzhenjie.permission.Permission

import com.yanzhenjie.permission.PermissionListener

import androidx.appcompat.app.AlertDialog
import android.os.Build


import android.net.Uri
import android.provider.MediaStore
import android.util.Log

import android.widget.Toast


import android.view.KeyEvent
import android.view.animation.LinearInterpolator
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import com.makeramen.roundedimageview.RoundedImageView
import com.wl.radio.util.ActivityUtil
import com.wl.radio.util.Constants
import com.wl.radio.util.Constants.BROADCAST_REFRESH_PLAY_RADIO_HISTORY
import com.wl.radio.util.ImgUtils
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.appnotification.NotificationColorUtils
import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException
import kotlinx.android.synthetic.main.activity_playing.*
import kotlinx.android.synthetic.main.layout_tab_play_item.*
import kotlinx.android.synthetic.main.layout_tab_play_item.view.*
import kotlinx.android.synthetic.main.layout_tab_play_item.view.riv_playing_radio


class MainActivity : BaseActivity() ,TabLayout.OnTabSelectedListener, IXmPlayerStatusListener {

   lateinit var mPlayerManager: XmPlayerManager
    var unSelectedTabPosition:Int=0
    lateinit var ivPlayingBottom: RoundedImageView
    var playingRadio:Radio?=null
   lateinit var animator: ObjectAnimator
    var broadcastReceiver:BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                Constants.BROADCAST_REFRESH_PLAY_RADIO_HISTORY->{
                    playingRadio=  intent.getParcelableExtra<Radio>(Constants.TRANS_PLAYING_RADIO)
                    playingRadio?.coverUrlLarge?.let { ImgUtils.showImage(this@MainActivity, it, ivPlayingBottom) }
                }
            }

        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewPagerAndTablayout()

        requestPermission()

        var intentFilter = IntentFilter()
        intentFilter.addAction(BROADCAST_REFRESH_PLAY_RADIO_HISTORY)
        registerReceiver(broadcastReceiver,intentFilter)

        mPlayerManager = XmPlayerManager.getInstance(this)
        val mNotification = XmNotificationCreater.getInstanse(this)
            .initNotification(this.applicationContext, PlayingActivity::class.java)
        NotificationColorUtils.isTargerSDKVersion24More = true;
        mPlayerManager?.init(System.currentTimeMillis().toInt(), mNotification)
        mPlayerManager?.addPlayerStatusListener(this)

        initAnim()
    }



    private fun initViewPagerAndTablayout() {
        Log.d("mainActivity","initViewPagerAndTablayout")
        var vpFragments: ArrayList<Fragment> = ArrayList<Fragment>()
        vpFragments.add(HomeFragment())
        vpFragments.add(NavigationFragment())
        vpFragments.add(PlayingFragment())
        vpFragments.add(CollectingFragment())
        vpFragments.add(MineFragment())

        viewPager?.adapter = MainVpAdapter(supportFragmentManager, vpFragments)
        initTab();


//        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(this);
        viewPager.offscreenPageLimit = 5


    }


    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        LogUtils.d("unselected","position:"+tab?.position)
        unSelectedTabPosition = tab?.position!!
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if(tab?.position===2){
            viewPager?.setCurrentItem(unSelectedTabPosition,false)
            tabLayout.getTabAt(unSelectedTabPosition)?.select()
            skipActivity(PlayingActivity::class.java);

        }else{
            viewPager?.setCurrentItem(tab?.position!!,false)
        }



    }



    private fun initTab() {
        tabLayout.addTab(
            tabLayout.newTab().setCustomView(
                normalTabIcon(
                    mainBottomTitles.get(0),
                    R.drawable.selector_tab_home
                )
            )
        )
        tabLayout.addTab(
            tabLayout.newTab().setCustomView(
                normalTabIcon(
                    mainBottomTitles.get(1),
                    R.drawable.selector_tab_navi
                )
            )
        )
        tabLayout.addTab(
            tabLayout.newTab().setCustomView(
                playingTabIcon()
            )
        )
        tabLayout.addTab(
            tabLayout.newTab().setCustomView(
                normalTabIcon(
                    mainBottomTitles.get(3),
                    R.drawable.selector_tab_collect
                )
            )
        )
        tabLayout.addTab(
            tabLayout.newTab().setCustomView(
                normalTabIcon(
                    mainBottomTitles.get(4),
                    R.drawable.selector_tab_mine
                )
            )
        )

    }

    private fun normalTabIcon(name: String, iconID: Int): View {
        LogUtils.d("mainActivityname", name)
        val newtab = LayoutInflater.from(this).inflate(R.layout.layout_tab_item, null)

        newtab.tvTabItem.text = name
        newtab.ivTabItem.setImageResource(iconID)

        return newtab
    }


    private fun playingTabIcon():View{
        val playTabIcon = LayoutInflater.from(this).inflate(R.layout.layout_tab_play_item,null);
        ivPlayingBottom = playTabIcon.riv_playing_radio


        return playTabIcon
    }


    private fun requestPermission() {
        // Fragment:
        AndPermission.with(this)
            .requestCode(100)
            .permission(

                Permission.STORAGE,Permission.LOCATION
            )
            .callback(permissionListener)
            .start()

    }

    private var hasRequestUrlsBefore = false

    private val permissionListener = object : PermissionListener {
        override fun onSucceed(requestCode: Int, grantedPermissions: List<String>) {
            // Successfully.
            // Successfully.
            if (requestCode == 100) {
                // TODO ...
                //                T.showShort(MainActivity.this,"successful");
                if (hasRequestUrlsBefore) {
                    return
                } else {
                    //权限成功等

                    hasRequestUrlsBefore = true
                }

            }
        }

        override fun onFailed(requestCode: Int, deniedPermissions: List<String>) {
            // Failure.
            if (requestCode == 100) {
                // TODO ...
                //                T.showShort(this,"failed");
                showToSettingDialog()
            }
        }
    }


    private fun showToSettingDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("友情提示")
        builder.setCancelable(false)
        builder.setMessage("运行缺少权限，请点击“设置”-“权限”打开所需权限")
        builder.setNegativeButton("取消", DialogInterface.OnClickListener { dialogInterface, i ->
            finish()
            System.exit(0)
        })
        builder.setPositiveButton("确定", DialogInterface.OnClickListener { dialogInterface, i -> ToSetting() })
        builder.show()

    }

    fun ToSetting() {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts("package", this.packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            localIntent.putExtra("com.android.settings.ApplicationPkgName", this.packageName)
        }
        startActivity(localIntent)

    }

    override fun onRestart() {
        super.onRestart()
        requestPermission()
    }

    private var firstTime: Long = 0

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {

        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                val secondTime = System.currentTimeMillis()
                if (secondTime - firstTime > 2000) {  //如果两次按下退出键的时差超过了两秒
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
                    firstTime = secondTime//更新firstTime


                    return true
                } else { //两次按下的时间差小于两秒时
                    //退出app
                   ActivityUtil.exitActivity(this@MainActivity)
                }
            }
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onPlayStart() {
        startRotateAnim()
        Log.d("MainActivity","onPlayStart")
    }

    override fun onSoundSwitch(p0: PlayableModel?, p1: PlayableModel?) {
        
    }

    override fun onPlayProgress(p0: Int, p1: Int) {
        
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onPlayPause() {
        stopRotateAnim()
        Log.d("MainActivity","onPlayPause")
    }

    override fun onBufferProgress(p0: Int) {
        
    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onPlayStop() {
        stopRotateAnim()
        Log.d("MainActivity","onPlayStop")
    }

    override fun onBufferingStart() {
        
    }

    override fun onSoundPlayComplete() {
        
    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onError(p0: XmPlayerException?): Boolean {
        stopRotateAnim()
        Log.d("MainActivity","onError")
        return false
    }

    override fun onSoundPrepared() {
        
    }

    override fun onBufferingStop() {
        
    }



    var isAnimRunning: Boolean = false
    fun initAnim() {
        animator = ObjectAnimator.ofFloat(ivPlayingBottom, "rotation", 0f, 360.0f);
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
    
}

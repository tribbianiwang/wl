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

import com.yanzhenjie.permission.Permission

import com.yanzhenjie.permission.PermissionListener

import android.content.DialogInterface

import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.os.Build


import android.net.Uri
import android.util.Log

import android.widget.Toast


import android.view.KeyEvent
import com.wl.radio.util.ActivityUtil


class MainActivity : BaseActivity() ,TabLayout.OnTabSelectedListener{

    var unSelectedTabPosition:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewPagerAndTablayout()

        requestPermission()

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
}

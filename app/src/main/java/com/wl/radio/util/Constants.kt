package com.wl.radio.util

import com.wl.radio.R

object Constants {
    val HIDE_LOADING_STATUS_MSG = "hide_loading_status_msg"
    val mainBottomTitles = arrayOf("首页", "导航", "播放","收藏","我的")

    val NAVITITLES = arrayOf("排行榜","城市台","新闻台","音乐台",
        "交通台","经济台","体育台","文艺台",
        "曲艺台","综合台","方言台","外语台",
        "生活台","都市台","旅游台","其他台"
    )

    val NAVIIMGS = arrayOf(R.drawable.category_rank,R.drawable.category_city,R.drawable.category_news,R.drawable.category_music,
        R.drawable.category_traffic,R.drawable.category_finance,R.drawable.category_sports,R.drawable.category_literature,
        R.drawable.category_opera,R.drawable.category_synthesize,R.drawable.category_dialect,R.drawable.category_language,
        R.drawable.category_life,R.drawable.category_urban,R.drawable.category_travel,R.drawable.category_other
        );








    val XMLYAPPKEY="9b6524dc6d6a39ebdc38eed8ea638c9f"
    val XMLYAPPPACKAGE="com.wl.radio"
    val XMLYAPPSECRET="de902aeaa6828dcaa5ea8c13b5be9ffd"


    val QUERYSTATUSLOADING="QUERYSTATUSLOADING"
    val QUERYSTATUSFAILED= "QUERYSTATUSFAILED"
    val QUERYSTATUSSUCCESS="QUERYSTATUSSUCCESS"

    val TOAST_DOUBLE_TIME_LIMIT = 1000


    val TRANSRADIO = "transradio"

     val CITYCODEHENAN=410000
    val RADIOTYPEINTERNET = 3
    val RADIOTYPEPROVINCE=2
    val RADIOTYPECOUNTRY = 1

    val CITYCODEDEFAULT=0

    val CITYCODE="citycode"
    val RADIOTYPE="radiotype";
    val CITYNAME="cityname"


    val BROADCASTSELECTRADIOIDCHANGE = "BROADCASTSELECTRADIOIDCHANGE"
    val SELECTRADIOID="SELECTRADIOID"

    val PULLTOREFRESHFIRSTGET=0
    val PULLTOREFRESHREFRESH=1
    val PULLTOREFRESHLOADMORE=2

    val CLOSE_APP_ACTION = "com.wl.radio.action_exit_app"


    val DEFAULTPAGECOUNT=20

    val DEFAULTRANKNUM=10

    val DEFAULTPAGE=1

    val ISQUERYCATEGORY="ISQUERYCATEGORY"
    val RADIOCATEGORYID="categoryid"

}
package com.wl.radio.bean

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio


@Entity(tableName = "collectRadio")
 data class CollectRadioBean(@ColumnInfo(name ="radioId")val radioId:String) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Long=0
}
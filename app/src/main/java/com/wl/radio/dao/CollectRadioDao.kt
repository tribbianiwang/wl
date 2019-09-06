package com.wl.radio.dao

import androidx.room.*
import com.wl.radio.bean.CollectRadioBean

@Dao
interface CollectRadioDao {
    //增加一个电台
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCollectRadio(collectRadio:CollectRadioBean)


    //增加多个电台
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCollectRadios(collectRadios:List<CollectRadioBean>)


    @Query("DELETE  FROM collectRadio WHERE radioId =:radioId")
    fun deleteCollectRadioById(radioId: String)

    //删除一个电台
    @Delete( )
    fun deleteCollectRadio(collectRadio:CollectRadioBean)

    //删除多个电台
    @Delete
    fun deleteCollectRadios(collectRadios:List<CollectRadioBean>)


    //更新一个电台
    @Update
    fun updateCollectRadio(collectRadio:CollectRadioBean)

    //更新多个电台
    @Update
    fun updateCollectRadios(collectRadios:List<CollectRadioBean>)


    //根据id查找电台
    @Query("SELECT * FROM collectRadio WHERE radioId =:radioId")
    fun queryCollectRadioById(radioId:String):CollectRadioBean


    @Query("SELECT * FROM collectRadio")
    fun queryAllCollectRadio():List<CollectRadioBean>




}
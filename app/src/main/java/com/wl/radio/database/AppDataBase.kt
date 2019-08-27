package com.wl.radio.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wl.radio.bean.User
import com.wl.radio.dao.UserDao

@Database(entities =[User::class],version = 1,exportSchema = false )
abstract class AppDataBase : RoomDatabase() {
    //获取到UserDao
    abstract fun userDao():UserDao

    companion object{
        @Volatile
        private var instance:AppDataBase? = null

        fun getInstance(context: Context):AppDataBase{
            return instance?: synchronized(this){
                instance?:buildDataBase(context)
                    .also {
                        instance = it
                    }
            }
        }


        private fun buildDataBase(context: Context):AppDataBase{
            return Room
                .databaseBuilder(context,AppDataBase::class.java,"wlradio-database")
                .addCallback(object :RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

//                        // 读取鞋的集合
//                        val request = OneTimeWorkRequestBuilder<ShoeWorker>().build()
//                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }
    }



}
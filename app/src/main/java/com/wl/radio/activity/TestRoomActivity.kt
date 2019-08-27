package com.wl.radio.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.wl.radio.MyApplication
import com.wl.radio.R
import com.wl.radio.bean.User
import com.wl.radio.dao.UserDao
import com.wl.radio.database.AppDataBase
import kotlinx.android.synthetic.main.activity_test_room.*
import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import kotlin.random.Random


class TestRoomActivity : AppCompatActivity() {
    val TAG = "TestRoomActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_room)




        bt_add.setOnClickListener {
            addUser(User("tudali","123"))
        }

        bt_add_userlist.setOnClickListener {
            var userList = ArrayList<User>()
            for( i in 1..10){
                userList.add(User("name"+i,"pwd"+i))
            }
            addUserList(userList)

        }

        bt_query_all.setOnClickListener {
            queryAllUser()

        }

    }


    fun addUser(user :User ){
        Observable.create<User> {

            MyApplication.userDao?.insertUser(user)
            it.onNext(user)

        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object:Subscriber<User>(){
                override fun onNext(t: User) {
                    Log.d(TAG,"onNext"+t.userName+"insert success")

                }

                override fun onCompleted() {


                }

                override fun onError(e: Throwable) {
                    Log.e(TAG,"onError"+e.message)

                }

            })


    }


    fun addUserList(users:List<User>){
        Observable.create<String> {
            MyApplication.userDao?.insertUsers(users)
            it.onNext("addlistSuccess")
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object:Subscriber<String>(){
                override fun onNext(t: String) {
                    Log.d(TAG,t)

                }

                override fun onCompleted() {


                }

                override fun onError(e: Throwable) {
                    Log.e(TAG,e.message)

                }

            })
    }



    fun queryAllUser(){
        Observable.create<User> {
           var users =  MyApplication.userDao.queryAllUsers()
            for(user in users){
                it.onNext(user)
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object:Subscriber<User>(){
                override fun onNext(t: User) {
                    Log.d(TAG,"queryresult:"+t.userName+"---"+t.password+"\n")


                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG,e.message)
                }

            })

    }



}

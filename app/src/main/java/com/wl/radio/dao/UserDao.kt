package com.wl.radio.dao

import androidx.room.*
import com.wl.radio.bean.User


@Dao
interface UserDao{
    //增加一个用户
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    //增加多个用户
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users:List<User>)

    //删除一个用户
    @Delete
    fun deleteUser(user:User)

    //删除多个用户
    @Delete
    fun deleteUsers(users:List<User>)

    //更新一个用户
    @Update
    fun updateUser(user:User)

    //更新多个用户
    @Update
    fun updateUsers(users:List<User>)


    //根据id查找用户
    @Query("SELECT * FROM user WHERE id=:id")
    fun queryUserById(id:Long):User?


    //根据用户名查找用户
    @Query("SELECT * FROM  user WHERE user_username=:userName")
    fun queryUserByUserName(userName:String):User?

    //返回所有的用户
    @Query("SELECT * FROM user")
    fun queryAllUsers():List<User>






}
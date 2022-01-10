package com.fintech.homework.database.objects.dao

import androidx.room.*
import com.fintech.homework.database.objects.entity.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Flowable<List<User>>

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): Flowable<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User): Completable

    @Delete
    fun delete(user: User): Completable
}
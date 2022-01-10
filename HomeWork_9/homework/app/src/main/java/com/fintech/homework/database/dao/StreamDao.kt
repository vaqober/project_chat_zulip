package com.fintech.homework.database.dao

import androidx.room.*
import com.fintech.homework.database.entity.Stream
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface StreamDao {
    @Query("SELECT * FROM stream")
    fun getAll(): Single<List<Stream>>

    @Query("SELECT * FROM stream WHERE subscribed = 1")
    fun getAllSubscribed(): Single<List<Stream>>

    @Query("SELECT * FROM stream WHERE subscribed = 0")
    fun loadAllUnsubscribed(): Maybe<List<Stream>>

    @Query("SELECT * FROM stream WHERE name LIKE '%'||:name||'%'")
    fun findByName(name: String): Maybe<List<Stream>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(streams: List<Stream>): Completable

    @Delete
    fun delete(stream: Stream): Completable
}
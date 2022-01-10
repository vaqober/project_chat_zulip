package com.fintech.finalwork.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fintech.finalwork.database.entity.Stream
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface StreamDao {
    @Query("SELECT * FROM stream")
    fun getAll(): Single<List<Stream>>

    @Query("SELECT * FROM stream WHERE subscribed = 1")
    fun getAllSubscribed(): Single<List<Stream>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(streams: List<Stream>): Completable
}
package com.fintech.finalwork.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fintech.finalwork.database.entity.Topic
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface TopicDao {
    @Query("SELECT * FROM topic WHERE parent_id IS (:streamId)")
    fun getAllByStreamId(streamId: Long): Single<List<Topic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(topics: List<Topic>): Completable
}
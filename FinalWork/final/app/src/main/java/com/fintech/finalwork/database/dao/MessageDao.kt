package com.fintech.finalwork.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fintech.finalwork.database.entity.MessageEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface MessageDao {
    @Query("SELECT * FROM message WHERE topic LIKE (:topic) AND stream_id = (:stream)")
    fun loadAllByStreamTopic(stream: Long, topic: String): Flowable<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: List<MessageEntity>): Completable
}
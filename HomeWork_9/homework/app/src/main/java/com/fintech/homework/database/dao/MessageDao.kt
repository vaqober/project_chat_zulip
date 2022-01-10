package com.fintech.homework.database.dao

import androidx.room.*
import com.fintech.homework.database.entity.MessageEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface MessageDao {

    @Query("SELECT * FROM message WHERE topic = (:topic) AND stream_id = (:stream)")
    fun loadAllByStreamTopic(stream: Long, topic: String): Flowable<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: List<MessageEntity>): Completable

    @Delete
    fun delete(topic: MessageEntity): Completable
}
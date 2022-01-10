package com.fintech.homework.database.objects.dao

import androidx.room.*
import com.fintech.homework.database.objects.entity.Topic
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface TopicDao {
    @Query("SELECT * FROM topic")
    fun getAll(): Maybe<List<Topic>>

    @Query("SELECT * FROM topic WHERE parent_id IS (:streamId)")
    fun loadAllByStreamId(streamId: Long): Maybe<List<Topic>>

    @Query("SELECT * FROM topic WHERE name LIKE '%' ||:name|| '%'")
    fun findByName(name: String): Maybe<List<Topic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(topics: List<Topic>): Completable

    @Delete
    fun delete(topic: Topic): Completable
}
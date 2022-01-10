package com.fintech.homework.database.objects.dao

import androidx.room.*
import com.fintech.homework.database.objects.entity.Topic
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface TopicDao {
    @Query("SELECT * FROM topic")
    fun getAll(): Single<List<Topic>>

    @Query("SELECT * FROM topic WHERE parent_id IS (:streamId)")
    fun getAllByStreamId(streamId: Long): Single<List<Topic>>

    @Query("SELECT * FROM topic WHERE name LIKE '%' ||:name|| '%'")
    fun findByName(name: String): Single<List<Topic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(topics: List<Topic>): Completable

    @Delete
    fun delete(topic: Topic): Completable
}
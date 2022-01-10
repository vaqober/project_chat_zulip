package com.fintech.homework.database.objects.repository

import com.fintech.homework.database.objects.entity.Topic
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

interface TopicRepository {
    fun findByName(name: String): Maybe<List<Topic>>
    fun loadAllByStream(stream: Long): Maybe<List<Topic>>
    fun insertAll(topics: List<Topic>): Completable
    fun delete(topic: Topic): Completable
}
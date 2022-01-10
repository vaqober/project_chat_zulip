package com.fintech.homework.database.objects.repository

import com.fintech.homework.database.objects.dao.TopicDao
import com.fintech.homework.database.objects.entity.Topic
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

class TopicRepositoryImpl(private val topicDao: TopicDao) : TopicRepository {
    override fun findByName(name: String): Maybe<List<Topic>> {
        return topicDao.findByName(name)
    }

    override fun loadAllByStream(stream: Long): Maybe<List<Topic>> {
        return topicDao.loadAllByStreamId(stream)
    }

    override fun insertAll(topics: List<Topic>): Completable {
        return topicDao.insertAll(topics)
    }

    override fun delete(topic: Topic): Completable {
        return topicDao.delete(topic)
    }
}
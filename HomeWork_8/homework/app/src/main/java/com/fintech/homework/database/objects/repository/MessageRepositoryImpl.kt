package com.fintech.homework.database.objects.repository

import com.fintech.homework.database.objects.dao.MessageDao
import com.fintech.homework.database.objects.entity.MessageEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class MessageRepositoryImpl(private val messageDao: MessageDao) : MessageRepository {

    override fun loadAllByStreamTopic(stream: Long, topic: String): Flowable<List<MessageEntity>> {
        return messageDao.loadAllByStreamTopic(stream, topic)
    }

    override fun insertAll(messages: List<MessageEntity>): Completable {
        return messageDao.insertAll(messages)
    }

    override fun delete(topic: MessageEntity): Completable {
        return messageDao.delete(topic)
    }
}
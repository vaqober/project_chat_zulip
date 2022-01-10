package com.fintech.homework.database.objects.repository

import com.fintech.homework.database.objects.entity.MessageEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface MessageRepository {
    fun loadAllByStreamTopic(stream: Long, topic: String): Flowable<List<MessageEntity>>
    fun insertAll(messages: List<MessageEntity>): Completable
    fun delete(topic: MessageEntity): Completable
}
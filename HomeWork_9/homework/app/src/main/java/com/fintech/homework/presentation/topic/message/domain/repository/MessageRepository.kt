package com.fintech.homework.presentation.topic.message.domain.repository

import com.fintech.homework.database.entity.MessageEntity
import com.fintech.homework.network.data.models.MessageInfo
import com.fintech.homework.network.data.models.ResponseBlankModel
import com.fintech.homework.network.data.models.ResponseIdModel
import com.fintech.homework.presentation.topic.domain.query.MessageQuery
import com.fintech.homework.presentation.topic.domain.query.ReactionQuery
import com.fintech.homework.presentation.topic.domain.query.SearchQuery
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface MessageRepository {
    fun getAllByStreamTopicDB(stream: Long, topic: String): Flowable<List<MessageEntity>>
    fun insertAllDB(messages: List<MessageEntity>): Completable
    fun delete(topic: MessageEntity): Completable
    fun loadMessages(searchQuery: SearchQuery): Single<List<MessageInfo>>
    fun postMessage(messageQuery: MessageQuery): Single<ResponseIdModel>
    fun postReaction(reactionQuery: ReactionQuery): Single<ResponseBlankModel>
    fun deleteReaction(reactionQuery: ReactionQuery): Single<ResponseBlankModel>
}
package com.fintech.finalwork.presentation.topic.message.domain.repository

import com.fintech.finalwork.database.entity.MessageEntity
import com.fintech.finalwork.network.data.models.MessageInfo
import com.fintech.finalwork.network.data.models.ResponseBlankModel
import com.fintech.finalwork.network.data.models.ResponseIdModel
import com.fintech.finalwork.presentation.topic.domain.query.MessageQuery
import com.fintech.finalwork.presentation.topic.domain.query.ReactionQuery
import com.fintech.finalwork.presentation.topic.domain.query.SearchQuery
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface MessageRepository {
    fun getAllByStreamTopicDB(stream: Long, topic: String): Flowable<List<MessageEntity>>
    fun insertAllDB(messages: List<MessageEntity>): Completable
    fun loadMessages(searchQuery: SearchQuery): Single<List<MessageInfo>>
    fun postMessage(messageQuery: MessageQuery): Single<ResponseIdModel>
    fun postReaction(reactionQuery: ReactionQuery): Single<ResponseBlankModel>
    fun deleteReaction(reactionQuery: ReactionQuery): Single<ResponseBlankModel>
}
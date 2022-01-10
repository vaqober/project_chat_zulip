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
import javax.inject.Inject

class LoadMessages @Inject constructor(
    private val messageRepository: MessageRepositoryImpl
) {
    fun subscribeToDBMessagesChange(stream: Long, topic: String): Flowable<List<MessageEntity>> {
        return messageRepository.getAllByStreamTopicDB(stream, topic)
    }

    fun loadMessages(searchQuery: SearchQuery): Single<List<MessageInfo>> {
        return messageRepository.loadMessages(searchQuery)
    }

    fun insertMessagesDB(messages: List<MessageEntity>): Completable {
        return messageRepository.insertAllDB(messages)
    }

    fun postMessage(messageQuery: MessageQuery): Single<ResponseIdModel> {
        return messageRepository.postMessage(messageQuery)
    }

    fun postReaction(reactionQuery: ReactionQuery): Single<ResponseBlankModel> {
        return messageRepository.postReaction(reactionQuery)
    }

    fun deleteReaction(reactionQuery: ReactionQuery): Single<ResponseBlankModel> {
        return messageRepository.deleteReaction(reactionQuery)
    }

}
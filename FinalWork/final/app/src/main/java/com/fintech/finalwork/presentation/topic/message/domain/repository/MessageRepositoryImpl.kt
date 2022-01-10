package com.fintech.finalwork.presentation.topic.message.domain.repository

import com.fintech.finalwork.database.dao.MessageDao
import com.fintech.finalwork.database.entity.MessageEntity
import com.fintech.finalwork.network.data.models.MessageInfo
import com.fintech.finalwork.network.data.models.ResponseBlankModel
import com.fintech.finalwork.network.data.models.ResponseIdModel
import com.fintech.finalwork.network.services.MessageService
import com.fintech.finalwork.network.services.ReactionService
import com.fintech.finalwork.presentation.topic.domain.query.MessageQuery
import com.fintech.finalwork.presentation.topic.domain.query.ReactionQuery
import com.fintech.finalwork.presentation.topic.domain.query.SearchQuery
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class MessageRepositoryImpl(
    private val messageService: MessageService,
    private val reactionService: ReactionService,
    private val messageDao: MessageDao
) : MessageRepository {

    override fun getAllByStreamTopicDB(stream: Long, topic: String): Flowable<List<MessageEntity>> {
        return messageDao.loadAllByStreamTopic(stream, topic)
    }

    override fun insertAllDB(messages: List<MessageEntity>): Completable {
        return messageDao.insertAll(messages)
    }

    override fun loadMessages(searchQuery: SearchQuery): Single<List<MessageInfo>> {
        return messageService.getMessages(
            searchQuery.anchor,
            searchQuery.numBefore,
            searchQuery.numAfter,
            searchQuery.getNarrow()
        ).map {
            it.messages.filter { message ->
                message.content.contains(searchQuery.searchQuery, ignoreCase = true)
            }
        }
    }

    override fun postMessage(messageQuery: MessageQuery): Single<ResponseIdModel> {
        return messageService.postMessage(
            messageQuery.type,
            messageQuery.to,
            messageQuery.topic,
            messageQuery.content
        )
    }

    override fun postReaction(reactionQuery: ReactionQuery): Single<ResponseBlankModel> {
        return reactionService.postReaction(
            messageId = reactionQuery.messageId,
            emojiName = reactionQuery.emojiName,
            reactionType = reactionQuery.reactionType
        )
    }

    override fun deleteReaction(reactionQuery: ReactionQuery): Single<ResponseBlankModel> {
        return reactionService.deleteReaction(
            messageId = reactionQuery.messageId,
            emojiName = reactionQuery.emojiName,
            reactionType = reactionQuery.reactionType
        )
    }
}
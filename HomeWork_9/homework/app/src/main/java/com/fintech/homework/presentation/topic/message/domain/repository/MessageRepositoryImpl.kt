package com.fintech.homework.presentation.topic.message.domain.repository

import com.fintech.homework.database.dao.MessageDao
import com.fintech.homework.database.entity.MessageEntity
import com.fintech.homework.network.data.models.MessageInfo
import com.fintech.homework.network.data.models.ResponseBlankModel
import com.fintech.homework.network.data.models.ResponseIdModel
import com.fintech.homework.presentation.topic.domain.query.MessageQuery
import com.fintech.homework.presentation.topic.domain.query.ReactionQuery
import com.fintech.homework.presentation.topic.domain.query.SearchQuery
import com.fintech.homework.network.services.MessageService
import com.fintech.homework.network.services.ReactionService
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

    override fun delete(topic: MessageEntity): Completable {
        return messageDao.delete(topic)
    }

    override fun loadMessages(searchQuery: SearchQuery): Single<List<MessageInfo>> {
        return messageService.getMessages(
            searchQuery.anchor,
            searchQuery.num_before,
            searchQuery.num_after,
            "[{\"operand\":${searchQuery.stream}, \"operator\":\"stream\"}," +
                    "{\"operator\": \"topic\", \"operand\": \"${searchQuery.subject}\"}]"
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
            message_id = reactionQuery.message_id,
            emoji_name = reactionQuery.emoji_name,
            reaction_type = reactionQuery.reaction_type
        )
    }

    override fun deleteReaction(reactionQuery: ReactionQuery): Single<ResponseBlankModel> {
        return reactionService.deleteReaction(
            message_id = reactionQuery.message_id,
            emoji_name = reactionQuery.emoji_name,
            reaction_type = reactionQuery.reaction_type
        )
    }
}
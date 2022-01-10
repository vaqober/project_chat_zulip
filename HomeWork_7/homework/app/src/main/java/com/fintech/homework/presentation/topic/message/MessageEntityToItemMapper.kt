package com.fintech.homework.presentation.topic.message

import com.fintech.homework.database.objects.entity.MessageEntity

internal class MessageEntityToItemMapper : (List<MessageEntity>) -> List<Message> {
    override fun invoke(messages: List<MessageEntity>): List<Message> {
        return messages.map { message ->
            Message(
                message.id,
                message.streamId,
                message.topic,
                message.senderId,
                message.senderName,
                message.content,
                message.timestamp,
                message.reactions.toMutableList()
            )
        }
    }
}
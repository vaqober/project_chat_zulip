package com.fintech.homework.presentation.topic.message

import com.fintech.homework.database.entity.MessageEntity

internal class MessageEntitysToItemMapper : (List<MessageEntity>) -> List<Message> {
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

internal class MessageEntityToItemMapper : (MessageEntity) -> Message {
    override fun invoke(message: MessageEntity): Message {
        return Message(
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

internal class MessageItemToEntityMapper : (Message) -> MessageEntity {
    override fun invoke(message: Message): MessageEntity {
        return MessageEntity(
            message.id,
            message.stream_id,
            message.topic,
            message.sender_id,
            message.sender_name,
            message.content,
            message.timestamp,
            message.reactions.toList()
        )
    }
}
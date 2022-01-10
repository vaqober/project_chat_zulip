package com.fintech.finalwork.presentation.topic.message

import android.text.Html
import com.fintech.finalwork.network.data.models.MessageInfo
import com.fintech.finalwork.presentation.topic.reaction.ReactionInfoToReactionMapper
import java.util.*

internal class MessagesInfoToItemMapper : (List<MessageInfo>) -> List<Message> {
    override fun invoke(messages: List<MessageInfo>): List<Message> {
        val reactionMapper = ReactionInfoToReactionMapper()
        return messages.map { message ->
            Message(
                message.id,
                message.streamId,
                message.subject,
                message.senderId,
                message.senderFullName,
                Html.fromHtml(message.content).toString().trim(),
                Date(message.timestamp * 1000),
                message.reactions.map(reactionMapper).toMutableList(),
                message.avatarUrl
            )
        }
    }
}

internal class MessageInfoToItemMapper : (MessageInfo) -> Message {
    override fun invoke(message: MessageInfo): Message {
        val reactionMapper = ReactionInfoToReactionMapper()
        return Message(
            message.id,
            message.streamId,
            message.subject,
            message.senderId,
            message.senderFullName,
            Html.fromHtml(message.content).toString().trim(),
            Date(message.timestamp * 1000),
            message.reactions.map(reactionMapper).toMutableList(),
            message.avatarUrl
        )
    }
}

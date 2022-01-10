package com.fintech.homework.presentation.topic.message

import android.text.Html
import com.fintech.homework.network.data.models.MessageInfo
import com.fintech.homework.presentation.topic.reaction.ReactionInfoToReactionMapper
import java.util.*

internal class MessageInfoToItemMapper : (List<MessageInfo>) -> List<Message> {
    override fun invoke(messages: List<MessageInfo>): List<Message> {
        val reactionMapper = ReactionInfoToReactionMapper()
        return messages.map { message ->
            Message(
                message.id,
                message.sender_id,
                message.sender_full_name,
                Html.fromHtml(message.content).toString().trim(),
                Date(message.timestamp * 1000),
                message.reactions.map(reactionMapper).toMutableList()
            )
        }
    }
}
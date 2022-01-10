package com.fintech.homework.presentation.topic.message

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework.presentation.topic.reaction.Reaction
import com.fintech.homework.presentation.topic.customview.CustomMessageViewGroup

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val messageView: CustomMessageViewGroup = view as CustomMessageViewGroup
    private var reactionsMap: MutableMap<String, MutableSet<Long>> = mutableMapOf()

    fun bind(message: Message, position: Int) {
        messageView.setNewMessage(message)
        messageView.position = position
        messageView.message = message
        reactionsMap = reactionListAsReactionById(message.reactions)
        messageView.updateReactions(reactionsMap)
    }

    private fun reactionListAsReactionById(reactions: List<Reaction>): MutableMap<String, MutableSet<Long>> {
        val mapEmoji: MutableMap<String, MutableSet<Long>> = mutableMapOf()
        for (i in reactions.indices) {
            val emoji = reactions[i].emoji_name
            if (mapEmoji.contains(emoji)) {
                mapEmoji.getValue(emoji).add(reactions[i].user_id)
            } else {
                mapEmoji[emoji] = mutableSetOf(reactions[i].user_id)
            }
        }
        return mapEmoji
    }
}
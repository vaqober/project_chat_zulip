package com.fintech.homework.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework.data.Message
import com.fintech.homework.customview.CustomMessageViewGroup
import com.fintech.homework.data.Reaction

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val messageView: CustomMessageViewGroup = view as CustomMessageViewGroup
    private var reactionsMap: MutableMap<String, MutableSet<Int>> = mutableMapOf()

    fun bind(message: Message, position: Int) {
        messageView.setNewMessage(message)
        messageView.position = position
        messageView.message = message
        reactionsMap = reactionListAsReactionById(message.reactions)
        messageView.updateReactions(reactionsMap)
    }

    private fun reactionListAsReactionById(reactions: List<Reaction>): MutableMap<String, MutableSet<Int>> {
        val mapEmoji: MutableMap<String, MutableSet<Int>> = mutableMapOf()
        for (i in reactions.indices) {
            val emoji = reactions[i].emoji
            if (mapEmoji.contains(emoji)) {
                mapEmoji.getValue(emoji).add(reactions[i].userId)
            } else {
                mapEmoji[emoji] = mutableSetOf(reactions[i].userId)
            }
        }
        return mapEmoji
    }
}
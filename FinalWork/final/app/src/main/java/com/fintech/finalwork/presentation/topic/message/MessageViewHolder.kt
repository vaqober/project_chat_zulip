package com.fintech.finalwork.presentation.topic.message

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.fintech.finalwork.R
import com.fintech.finalwork.network.GlideProvider
import com.fintech.finalwork.network.GlideProviderImpl
import com.fintech.finalwork.presentation.topic.customview.CustomMessageViewGroup
import com.fintech.finalwork.presentation.topic.reaction.Reaction
import javax.inject.Inject

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val messageView: CustomMessageViewGroup = view as CustomMessageViewGroup
    private var reactionsMap: MutableMap<String, MutableSet<Long>> = mutableMapOf()

    var glide: GlideProvider = GlideProviderImpl()
        @Inject set

    fun bind(message: Message, position: Int) {
        messageView.setNewMessage(message)
        messageView.position = position
        messageView.message = message
        reactionsMap = reactionListAsReactionById(message.reactions)
        messageView.updateReactions(reactionsMap)
        val imageView = messageView.findViewById<ImageView>(R.id.userImage)
        glide.loadImage(message.avatarUrl, imageView, true)
    }

    fun bindEmoji(message: Message, position: Int) {
        reactionsMap = reactionListAsReactionById(message.reactions)
        messageView.updateReactions(reactionsMap)
    }

    private fun reactionListAsReactionById(reactions: List<Reaction>): MutableMap<String, MutableSet<Long>> {
        val mapEmoji: MutableMap<String, MutableSet<Long>> = mutableMapOf()
        for (i in reactions.indices) {
            val emoji = reactions[i].emojiName
            if (mapEmoji.contains(emoji)) {
                mapEmoji.getValue(emoji).add(reactions[i].userId)
            } else {
                mapEmoji[emoji] = mutableSetOf(reactions[i].userId)
            }
        }
        return mapEmoji
    }
}
package com.fintech.homework.presentation.topic.reaction

import com.fintech.homework.network.data.models.ReactionInfo
import com.fintech.homework.objects.Emoji
import java.util.*

internal class ReactionInfoToReactionMapper : (ReactionInfo) -> Reaction {
    override fun invoke(reaction: ReactionInfo): Reaction {
        val emojiCode = try {
            Emoji.valueOf(reaction.emoji_name.toUpperCase(Locale.ROOT)).unicode
        } catch (e: IllegalArgumentException) {
            reaction.emoji_code
        }
        return Reaction(reaction.emoji_name, emojiCode, reaction.user_id)
    }
}
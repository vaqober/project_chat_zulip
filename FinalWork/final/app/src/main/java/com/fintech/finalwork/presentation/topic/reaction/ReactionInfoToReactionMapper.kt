package com.fintech.finalwork.presentation.topic.reaction

import com.fintech.finalwork.network.data.models.ReactionInfo
import com.fintech.finalwork.objects.Emoji
import java.util.*

internal class ReactionInfoToReactionMapper : (ReactionInfo) -> Reaction {
    override fun invoke(reaction: ReactionInfo): Reaction {
        val emojiCode = try {
            Emoji.valueOf(reaction.emojiName.uppercase(Locale.ROOT)).unicode
        } catch (e: IllegalArgumentException) {
            reaction.emojiCode
        }
        return Reaction(reaction.emojiName, emojiCode, reaction.userId)
    }
}
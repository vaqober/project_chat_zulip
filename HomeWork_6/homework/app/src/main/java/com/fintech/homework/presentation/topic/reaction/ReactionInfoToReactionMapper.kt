package com.fintech.homework.presentation.topic.reaction

import com.fintech.homework.objects.Emoji
import com.fintech.homework.network.data.models.ReactionInfo
import java.util.*

internal class ReactionInfoToReactionMapper : (ReactionInfo) -> Reaction {
    override fun invoke(reaction: ReactionInfo): Reaction {
        return Reaction(reaction.emoji_name, Emoji.valueOf(reaction.emoji_name.toUpperCase(Locale.ROOT)).unicode, reaction.user_id)
    }
}
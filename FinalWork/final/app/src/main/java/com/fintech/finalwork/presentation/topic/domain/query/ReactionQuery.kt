package com.fintech.finalwork.presentation.topic.domain.query

data class ReactionQuery(
    val emojiName: String,
    val reactionType: String,
    val messageId: Long
)
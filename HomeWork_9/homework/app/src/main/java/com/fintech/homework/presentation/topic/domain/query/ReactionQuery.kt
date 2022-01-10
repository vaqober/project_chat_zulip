package com.fintech.homework.presentation.topic.domain.query

data class ReactionQuery(
    val emoji_name: String,
    val reaction_type: String,
    val message_id: Long
)
package com.fintech.homework.network.data.models.query

data class ReactionQuery(
    val emoji_name: String,
    val reaction_type: String,
    val message_id: Long,
    val action: Action
)

enum class Action {
    POST,
    DELETE
}
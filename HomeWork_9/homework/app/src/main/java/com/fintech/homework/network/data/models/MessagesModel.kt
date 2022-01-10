package com.fintech.homework.network.data.models

import kotlinx.serialization.Serializable

@Serializable
data class MessagesResponse(
    val result: String,
    val msg: String,
    val messages: List<MessageInfo>
)

@Serializable
data class MessageInfo(
    val id: Long,
    val sender_id: Long,
    val content: String,
    val recipient_id: Int,
    val timestamp: Long,
    val client: String,
    val subject: String,
    val topic_links: List<String>,
    val is_me_message: Boolean,
    val reactions: List<ReactionInfo>,
    val submessages: List<String>,
    val flags: List<String>,
    val sender_full_name: String,
    val sender_email: String,
    val sender_realm_str: String,
    val display_recipient: String,
    val type: String,
    val stream_id: Long,
    val avatar_url: String,
    val content_type: String
)

@Serializable
data class ReactionInfo(
val emoji_name: String,
val emoji_code: String,
val reaction_type: String,
val user: UserInfo,
val user_id: Long
)

@Serializable
data class UserInfo(
    val email: String,
    val id: Long,
    val full_name: String
)
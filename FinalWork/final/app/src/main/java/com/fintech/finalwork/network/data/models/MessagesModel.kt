package com.fintech.finalwork.network.data.models

import kotlinx.serialization.SerialName
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
    @SerialName("sender_id")
    val senderId: Long,
    val content: String,
    @SerialName("recipient_id")
    val recipientId: Int,
    val timestamp: Long,
    val client: String,
    val subject: String,
    @SerialName("topic_links")
    val topicLinks: List<String>,
    @SerialName("is_me_message")
    val isMeMessage: Boolean,
    val reactions: List<ReactionInfo>,
    val submessages: List<String>,
    val flags: List<String>,
    @SerialName("sender_full_name")
    val senderFullName: String,
    @SerialName("sender_email")
    val senderEmail: String,
    @SerialName("sender_realm_str")
    val senderRealmStr: String,
    @SerialName("display_recipient")
    val displayRecipient: String,
    val type: String,
    @SerialName("stream_id")
    val streamId: Long,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("content_type")
    val contentType: String
)

@Serializable
data class ReactionInfo(
    @SerialName("emoji_name")
    val emojiName: String,
    @SerialName("emoji_code")
    val emojiCode: String,
    @SerialName("reaction_type")
    val reactionType: String,
    val user: UserInfo,
    @SerialName("user_id")
    val userId: Long
)

@Serializable
data class UserInfo(
    val email: String,
    val id: Long,
    @SerialName("full_name")
    val fullName: String
)
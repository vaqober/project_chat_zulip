package com.fintech.finalwork.network.data.models

import kotlinx.parcelize.RawValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class StreamsResponse(
    val result: String,
    val msg: String,
    val streams: @RawValue List<StreamInfo>
)

@Serializable
data class SubscriptionsResponse(
    val result: String,
    val msg: String,
    val subscriptions: @RawValue List<StreamInfo>
)

@Serializable
data class StreamInfo(
    val name: String,
    @SerialName("stream_id")
    val streamId: Long,
    val description: String? = null,
    @SerialName("rendered_description")
    val renderedDescription: String? = null,
    @SerialName("invite_only")
    val inviteOnly: Boolean? = null,
    @SerialName("is_web_public")
    val isWebPublic: Boolean? = null,
    @SerialName("stream_post_policy")
    val streamPostPolicy: Int? = null,
    @SerialName("history_public_to_subscribers")
    val historyPublicToSubscribers: Boolean? = null,
    @SerialName("first_message_id")
    val firstMessageId: Long? = null,
    @SerialName("message_retention_days")
    val messageRetentionDays: Int? = null,
    @SerialName("date_created")
    val dateCreated: Long? = null,
    @SerialName("is_announcement_only")
    val isAnnouncementOnly: Boolean? = null
)

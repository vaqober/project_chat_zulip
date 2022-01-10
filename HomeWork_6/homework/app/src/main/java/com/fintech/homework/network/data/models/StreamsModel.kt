package com.fintech.homework.network.data.models

import kotlinx.serialization.Serializable


@Serializable
data class StreamsResponse(
    val result: String,
    val msg: String,
    val streams: List<StreamInfo>
)

@Serializable
data class StreamInfo(
    val name: String,
    val stream_id: Long,
    val description: String,
    val rendered_description: String,
    val invite_only: Boolean,
    val is_web_public: Boolean,
    val stream_post_policy: Int,
    val history_public_to_subscribers: Boolean,
    val first_message_id: Long,
    val message_retention_days: Int?,
    val date_created: Long,
    val is_announcement_only: Boolean
)

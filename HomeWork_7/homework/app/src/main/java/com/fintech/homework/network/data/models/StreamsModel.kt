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
    val description: String? = null,
    val rendered_description: String? = null,
    val invite_only: Boolean? = null,
    val is_web_public: Boolean? = null,
    val stream_post_policy: Int? = null,
    val history_public_to_subscribers: Boolean? = null,
    val first_message_id: Long? = null,
    val message_retention_days: Int? = null,
    val date_created: Long? = null,
    val is_announcement_only: Boolean? = null
)

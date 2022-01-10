package com.fintech.finalwork.network.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseBlankModel(
    val result: String
)

@Serializable
data class ResponseIdModel(
    val result: String,
    val id: Long
)

@Serializable
data class ResponseSubscribe(
    @SerialName("already_subscribed")
    val alreadySubscribed: MutableMap<String, List<String>>,
    val msg: String,
    val result: String,
    val subscribed: MutableMap<String, List<String>>,
)
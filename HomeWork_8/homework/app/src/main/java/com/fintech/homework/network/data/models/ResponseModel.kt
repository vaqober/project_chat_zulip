package com.fintech.homework.network.data.models

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
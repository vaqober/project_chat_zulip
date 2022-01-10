package com.fintech.finalwork.network.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeopleResponse(
    val result: String,
    val msg: String,
    val members: List<PersonInfo>
)

@Serializable
data class PersonInfo(
    val email: String,
    @SerialName("user_id")
    val userId: Long,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("avatar_url")
    val avatarUrl: String
)
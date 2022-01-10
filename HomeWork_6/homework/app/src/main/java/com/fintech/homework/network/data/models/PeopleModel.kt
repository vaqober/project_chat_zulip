package com.fintech.homework.network.data.models

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
    val user_id: Long,
    val full_name: String,
    val avatar_url: String
)
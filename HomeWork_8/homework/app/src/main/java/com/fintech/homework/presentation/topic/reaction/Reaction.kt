package com.fintech.homework.presentation.topic.reaction

import kotlinx.serialization.Serializable

@Serializable
data class Reaction(val emoji_name: String, val emoji_code: String, val user_id: Long)

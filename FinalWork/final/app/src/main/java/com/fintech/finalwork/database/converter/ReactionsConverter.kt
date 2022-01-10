package com.fintech.finalwork.database.converter

import androidx.room.TypeConverter
import com.fintech.finalwork.presentation.topic.reaction.Reaction
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ReactionsConverter {
    @TypeConverter
    fun fromReactions(reactions: List<Reaction>) = Json.encodeToString(reactions)

    @TypeConverter
    fun toReactions(data: String): List<Reaction> {
        return Json.decodeFromString(data)
    }
}
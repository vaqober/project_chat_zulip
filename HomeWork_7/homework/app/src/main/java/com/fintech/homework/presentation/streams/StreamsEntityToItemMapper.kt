package com.fintech.homework.presentation.streams

import com.fintech.homework.database.objects.entity.Stream
import com.fintech.homework.presentation.streams.objects.StreamTopicItem
import com.fintech.homework.presentation.streams.objects.Types

internal class StreamsEntityToItemMapper : (List<Stream>) -> List<StreamTopicItem> {
    override fun invoke(streams: List<Stream>): List<StreamTopicItem> {
        return streams.map { stream ->
            StreamTopicItem(stream.id, -1, stream.name, Types.VIEW_TYPE_CHANNEL)
        }
    }
}
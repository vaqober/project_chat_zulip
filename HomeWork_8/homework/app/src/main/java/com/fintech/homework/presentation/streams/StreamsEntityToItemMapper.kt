package com.fintech.homework.presentation.streams

import com.fintech.homework.database.objects.entity.Stream
import com.fintech.homework.presentation.streams.objects.StreamTopicItem
import com.fintech.homework.presentation.streams.objects.Types

internal class StreamEntityToItemMapper : (Stream) -> StreamTopicItem {
    override fun invoke(stream: Stream): StreamTopicItem {
        return  StreamTopicItem(stream.id, -1, stream.name, Types.VIEW_TYPE_CHANNEL)
    }
}
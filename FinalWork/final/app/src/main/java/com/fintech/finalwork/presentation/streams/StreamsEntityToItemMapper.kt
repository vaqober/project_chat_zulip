package com.fintech.finalwork.presentation.streams

import com.fintech.finalwork.database.entity.Stream
import com.fintech.finalwork.presentation.streams.objects.StreamTopicItem
import com.fintech.finalwork.presentation.streams.objects.Types

internal class StreamEntityToItemMapper : (Stream) -> StreamTopicItem {
    override fun invoke(stream: Stream): StreamTopicItem {
        return StreamTopicItem(stream.id, -1, stream.name, Types.VIEW_TYPE_CHANNEL)
    }
}
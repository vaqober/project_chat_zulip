package com.fintech.homework.presentation.streams

import com.fintech.homework.network.data.models.StreamInfo
import com.fintech.homework.presentation.streams.objects.StreamTopicItem
import com.fintech.homework.presentation.streams.objects.Types

internal class StreamToItemMapper : (StreamInfo) -> StreamTopicItem {
    override fun invoke(stream: StreamInfo): StreamTopicItem {
        return StreamTopicItem(
            stream.stream_id,
            -1,
            "#${stream.name}",
            Types.VIEW_TYPE_CHANNEL
        )
    }
}
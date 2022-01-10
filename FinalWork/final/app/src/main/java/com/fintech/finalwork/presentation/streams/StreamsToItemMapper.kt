package com.fintech.finalwork.presentation.streams

import com.fintech.finalwork.network.data.models.StreamInfo
import com.fintech.finalwork.presentation.streams.objects.StreamTopicItem
import com.fintech.finalwork.presentation.streams.objects.Types

internal class StreamToItemMapper : (StreamInfo) -> StreamTopicItem {
    override fun invoke(stream: StreamInfo): StreamTopicItem {
        return StreamTopicItem(
            stream.streamId,
            -1,
            "#${stream.name}",
            Types.VIEW_TYPE_CHANNEL
        )
    }
}
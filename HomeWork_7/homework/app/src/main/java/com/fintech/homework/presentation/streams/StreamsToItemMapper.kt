package com.fintech.homework.presentation.streams

import com.fintech.homework.network.data.models.StreamInfo
import com.fintech.homework.presentation.streams.objects.StreamTopicItem
import com.fintech.homework.presentation.streams.objects.Types

internal class StreamsToItemMapper : (List<StreamInfo>) -> List<StreamTopicItem> {
    override fun invoke(streams: List<StreamInfo>): List<StreamTopicItem> {
        return streams.map { stream ->
            StreamTopicItem(stream.stream_id, -1, "#${stream.name}", Types.VIEW_TYPE_CHANNEL)
        }
    }
}
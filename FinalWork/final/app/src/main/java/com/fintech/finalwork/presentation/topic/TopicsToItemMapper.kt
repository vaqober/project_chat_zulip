package com.fintech.finalwork.presentation.topic

import com.fintech.finalwork.presentation.streams.objects.StreamTopicItem
import com.fintech.finalwork.presentation.streams.objects.Topic
import com.fintech.finalwork.presentation.streams.objects.Types.VIEW_TYPE_TOPIC

internal class TopicToItemMapper : (Topic) -> (StreamTopicItem) {
    override fun invoke(topic: Topic): StreamTopicItem {
        return StreamTopicItem(-1, topic.parent, topic.name, VIEW_TYPE_TOPIC)
    }
}
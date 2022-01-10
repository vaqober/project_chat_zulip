package com.fintech.homework.presentation.topic

import com.fintech.homework.presentation.streams.objects.StreamTopicItem
import com.fintech.homework.presentation.streams.objects.Topic
import com.fintech.homework.presentation.streams.objects.Types.VIEW_TYPE_TOPIC

internal class TopicToItemMapper : (Topic) -> (StreamTopicItem){
    override fun invoke(topic: Topic): StreamTopicItem {
        return StreamTopicItem(topic.id, topic.parent, topic.name, VIEW_TYPE_TOPIC)
    }
}
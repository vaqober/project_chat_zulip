package com.fintech.homework.presentation.topic

import com.fintech.homework.presentation.streams.objects.Types.VIEW_TYPE_TOPIC
import com.fintech.homework.presentation.streams.objects.StreamTopicItem
import com.fintech.homework.presentation.streams.objects.Topic

internal class TopicToItemMapper : (List<Topic>) -> (List<StreamTopicItem>){
    override fun invoke(topics: List<Topic>): List<StreamTopicItem> {
        return topics.map { topic ->
            StreamTopicItem(topic.id, topic.parent, topic.name, VIEW_TYPE_TOPIC)
        }
    }
}
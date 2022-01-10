package com.fintech.homework.presentation.topic

import com.fintech.homework.database.objects.entity.Topic
import com.fintech.homework.presentation.streams.objects.StreamTopicItem
import com.fintech.homework.presentation.streams.objects.Types

internal class TopicsEntityToItemMapper : (List<Topic>) -> List<StreamTopicItem> {
    override fun invoke(streams: List<Topic>): List<StreamTopicItem> {
        return streams.map { topic ->
            StreamTopicItem(topic.id, topic.parentId, topic.name, Types.VIEW_TYPE_TOPIC)
        }
    }
}
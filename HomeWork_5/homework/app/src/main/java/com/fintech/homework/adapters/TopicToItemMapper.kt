package com.fintech.homework.adapters

import com.fintech.homework.adapters.Types.VIEW_TYPE_TOPIC
import com.fintech.homework.data.ChannelTopicItem
import com.fintech.homework.data.Topic

internal class TopicToItemMapper : (List<Topic>) -> (List<ChannelTopicItem>){
    override fun invoke(topics: List<Topic>): List<ChannelTopicItem> {
        return topics.map { topic ->
            ChannelTopicItem(topic.id, topic.parentId, topic.name, VIEW_TYPE_TOPIC)
        }
    }
}
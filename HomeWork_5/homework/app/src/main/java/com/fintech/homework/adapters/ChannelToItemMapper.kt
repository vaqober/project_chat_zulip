package com.fintech.homework.adapters

import com.fintech.homework.data.Channel
import com.fintech.homework.data.ChannelTopicItem

internal class ChannelToItemMapper : (List<Channel>) -> List<ChannelTopicItem> {
    override fun invoke(channels: List<Channel>): List<ChannelTopicItem> {
        return channels.map { channel ->
            ChannelTopicItem(channel.id, -1,channel.name, Types.VIEW_TYPE_CHANNEL)
        }
    }
}
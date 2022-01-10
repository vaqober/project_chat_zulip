package com.fintech.homework_4.data

object Channels {
    fun getStubChannelList(): MutableList<Channel> {
        val list = mutableListOf<Topic>()
        val channelList = mutableListOf<Channel>()
        for (i in 0..10) {
            list.add(Topics.getStubTopic(i))
        }
        for (i in 0..10) {
            channelList.add(Channel(i, "#general $i", list))
        }
        return channelList
    }
}
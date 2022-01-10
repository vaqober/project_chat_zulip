package com.fintech.homework.data

data class ChannelTopicItem(val id: Int, val parentId: Int = -1, val name: String, val type: Int, var isExpanded: Boolean = false)
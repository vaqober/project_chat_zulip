package com.fintech.homework.presentation.streams.objects

data class StreamTopicItem(val id: Long, val parentId: Long = -1, val name: String, val type: Int, var isExpanded: Boolean = false)
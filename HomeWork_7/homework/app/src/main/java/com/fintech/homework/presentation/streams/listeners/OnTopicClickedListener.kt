package com.fintech.homework.presentation.streams.listeners

interface OnTopicClickedListener {
    fun onTopicClicked(stream: Long, subject: String)
}
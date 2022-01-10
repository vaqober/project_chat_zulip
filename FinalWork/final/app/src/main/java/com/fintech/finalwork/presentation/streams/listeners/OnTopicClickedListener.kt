package com.fintech.finalwork.presentation.streams.listeners

interface OnTopicClickedListener {
    fun onTopicClicked(stream: Long, subject: String)
}
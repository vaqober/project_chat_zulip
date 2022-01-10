package com.fintech.homework.data

internal sealed class ScreenState {

    class Result(val items: List<ChannelTopicItem>) : ScreenState()

    object Loading : ScreenState()

    class Error(val error: Throwable) : ScreenState()
}
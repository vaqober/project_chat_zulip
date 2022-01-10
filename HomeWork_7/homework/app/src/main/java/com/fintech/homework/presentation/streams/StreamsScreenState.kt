package com.fintech.homework.presentation.streams

import com.fintech.homework.presentation.streams.objects.StreamTopicItem

internal sealed class StreamsScreenState {

    class Result(val items: List<StreamTopicItem>) : StreamsScreenState()

    object Loading : StreamsScreenState()

    class Error(val error: Throwable) : StreamsScreenState()
}
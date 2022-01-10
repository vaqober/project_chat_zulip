package com.fintech.homework.presentation.topic.message

internal sealed class MessageScreenState {

    class Result(val items: List<Message>) : MessageScreenState()

    object Loading : MessageScreenState()

    class Error(val error: Throwable) : MessageScreenState()
}
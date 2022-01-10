package com.fintech.homework.data

internal sealed class MessageScreenState {

    class Result(val items: List<Message>) : MessageScreenState()

    object Loading : MessageScreenState()

    class Error(val error: Throwable) : MessageScreenState()
}
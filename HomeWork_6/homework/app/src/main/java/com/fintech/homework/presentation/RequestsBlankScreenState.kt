package com.fintech.homework.presentation

import com.fintech.homework.network.data.models.ResponseBlankModel

internal sealed class RequestsBlankScreenState {

    class Result(val response: ResponseBlankModel) : RequestsBlankScreenState()

    object Loading : RequestsBlankScreenState()

    class Error(val error: Throwable) : RequestsBlankScreenState()
}
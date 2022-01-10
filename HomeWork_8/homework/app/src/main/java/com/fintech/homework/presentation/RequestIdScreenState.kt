package com.fintech.homework.presentation

import com.fintech.homework.network.data.models.ResponseIdModel

internal sealed class RequestsIdScreenState {

    class Result(val response: ResponseIdModel) : RequestsIdScreenState()

    object Loading : RequestsIdScreenState()

    class Error(val error: Throwable) : RequestsIdScreenState()
}
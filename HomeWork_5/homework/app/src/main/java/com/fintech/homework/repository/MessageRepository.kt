package com.fintech.homework.repository

import com.fintech.homework.data.Message
import io.reactivex.rxjava3.core.Observable

interface MessageRepository {
    fun loadMessages(): Observable<List<Message>>
}
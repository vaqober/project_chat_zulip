package com.fintech.homework.repository

import com.fintech.homework.data.Channel
import io.reactivex.rxjava3.core.Observable

interface ChannelRepository {
    fun loadChannels() : Observable<List<Channel>>
}
package com.fintech.homework.network.services

import com.fintech.homework.network.data.models.StreamsResponse
import com.fintech.homework.network.data.models.SubscriptionsResponse
import com.fintech.homework.network.data.models.TopicsResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface StreamsService {

    @GET("streams")
    fun loadAllStreams(): Single<StreamsResponse>

    @GET("users/me/subscriptions")
    fun loadSubscribedStreams(): Single<SubscriptionsResponse>

    @GET("users/me/{stream_id}/topics")
    fun loadTopicsByStreamId(@Path("stream_id") stream: Long): Single<TopicsResponse>
}
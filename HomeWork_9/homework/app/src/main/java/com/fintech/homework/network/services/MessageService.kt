package com.fintech.homework.network.services

import com.fintech.homework.network.data.models.MessagesResponse
import com.fintech.homework.network.data.models.ResponseIdModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface MessageService {
    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_before") num_before: Int,
        @Query("num_after") num_after: Int,
        @Query("narrow") narrow: String
    ): Single<MessagesResponse>

    @FormUrlEncoded
    @POST("messages")
    fun postMessage(
        @Field("type") type: String,
        @Field("to") to: List<Long>,
        @Field("topic") topic: String,
        @Field("content") content: String
    ): Single<ResponseIdModel>
}
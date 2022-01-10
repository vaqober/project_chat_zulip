package com.fintech.finalwork.network.services

import com.fintech.finalwork.network.data.models.ResponseBlankModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface ReactionService {

    @FormUrlEncoded
    @POST("messages/{messageId}/reactions")
    fun postReaction(
        @Path("messageId") messageId: Long,
        @Field("emoji_name") emojiName: String,
        @Field("reaction_type") reactionType: String
    ): Single<ResponseBlankModel>

    @DELETE("messages/{messageId}/reactions")
    fun deleteReaction(
        @Path("messageId") messageId: Long,
        @Query("emoji_name") emojiName: String,
        @Query("reaction_type") reactionType: String
    ): Single<ResponseBlankModel>
}
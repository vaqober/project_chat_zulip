package com.fintech.homework.network.usecase

import com.fintech.homework.network.data.models.ResponseBlankModel
import com.fintech.homework.network.data.models.query.ReactionQuery
import io.reactivex.rxjava3.core.Single

interface ReactionUseCase : (ReactionQuery) -> Single<ResponseBlankModel> {
    override fun invoke(reactionQuery: ReactionQuery): Single<ResponseBlankModel>
}
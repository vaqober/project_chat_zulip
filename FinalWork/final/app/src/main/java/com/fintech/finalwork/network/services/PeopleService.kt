package com.fintech.finalwork.network.services

import com.fintech.finalwork.network.data.models.PeopleResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface PeopleService {

    @GET("users")
    fun getAllPeople(): Single<PeopleResponse>
}
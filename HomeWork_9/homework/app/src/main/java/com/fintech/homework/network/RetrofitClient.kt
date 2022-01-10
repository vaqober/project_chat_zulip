package com.fintech.homework.network

import com.fintech.homework.network.services.StreamsService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory


object RetrofitClient {

    private const val BASE_URL = "https://tinkoff-android-fall21.zulipchat.com/api/v1/"
    private val contentType = "application/json".toMediaType()
    private val jsonProperty = Json { ignoreUnknownKeys = true}

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .authenticator { _: Route?, response: Response ->
            val request: Request = response.request
            if (request.header("Authorization") != null) // Логин и пароль неверны
                return@authenticator null
            request.newBuilder()
                .header(
                    "Authorization",
                    Credentials.basic(
                        "testBotTimofey-bot@tinkoff-android-fall21.zulipchat.com",
                        "U9ZVVlyT9M9QJ2YR8GObXoaGUamkkF1l"
                    )
                )
                .build()
        }
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(jsonProperty.asConverterFactory(contentType))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    val serviceStreams: StreamsService = retrofit.create(StreamsService::class.java)
}
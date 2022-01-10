package com.fintech.finalwork.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

@Module
class RetrofitModule {

    @Provides
    fun provideBaseUrl(): String = "https://tinkoff-android-fall21.zulipchat.com/api/v1/"

    private val contentType = "application/json".toMediaType()
    private val jsonProperty = Json { ignoreUnknownKeys = true }

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .authenticator { _: Route?, response: Response ->
            val request: Request = response.request
            if (request.header("Authorization") != null) // Логин и пароль неверны
                return@authenticator null
            request.newBuilder()
                .header(
                    "Authorization",
                    Credentials.basic(
                        "t-kabantsov@mail.ru",
                        "QWDlR2kza1DOmiOoA8l1rTVOthTCEiBA"
                    )
                )
                .build()
        }
        .build()

    @Provides
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(jsonProperty.asConverterFactory(contentType))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
}
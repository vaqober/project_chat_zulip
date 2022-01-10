package com.fintech.homework.network

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import dagger.Module
import dagger.Provides
import okhttp3.*
import java.io.InputStream


@Module
@GlideModule
@Excludes(OkHttpLibraryGlideModule::class)
class GlideModule : AppGlideModule() {

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

    @Provides
    fun provideGlide(okHttpClient: OkHttpClient) : GlideProvider = GlideProviderImpl(okHttpClient)

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        registry.replace(GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient))
    }
}
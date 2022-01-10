package com.fintech.finalwork

import android.content.Context
import com.fintech.finalwork.presentation.streams.StreamsFragment
import com.fintech.finalwork.presentation.topic.TopicFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun injectStreamFragment(streamsFragment: StreamsFragment)

    fun injectTopicFragment(topicFragment: TopicFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            context: Context
        ): AppComponent
    }
}
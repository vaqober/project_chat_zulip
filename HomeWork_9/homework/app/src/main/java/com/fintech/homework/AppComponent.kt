package com.fintech.homework

import android.content.Context
import com.fintech.homework.presentation.streams.StreamsFragment
import com.fintech.homework.presentation.topic.TopicFragment
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
package com.fintech.homework.presentation.topic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fintech.homework.App
import com.fintech.homework.database.objects.entity.Topic
import com.fintech.homework.database.objects.repository.TopicRepository
import com.fintech.homework.database.objects.repository.TopicRepositoryImpl
import com.fintech.homework.network.usecase.search.SearchTopicsUseCase
import com.fintech.homework.network.usecase.search.SearchTopicsUseCaseImpl
import com.fintech.homework.presentation.streams.StreamsScreenState
import com.fintech.homework.presentation.streams.objects.StreamTopicItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

internal class TopicsViewModel : ViewModel() {

    private val _streamsScreenState: MutableLiveData<StreamsScreenState> = MutableLiveData()
    val streamsScreenState: LiveData<StreamsScreenState>
        get() = _streamsScreenState
    private val _streamsDBScreenState: MutableLiveData<StreamsScreenState> = MutableLiveData()
    val streamsDBScreenState: LiveData<StreamsScreenState>
        get() = _streamsDBScreenState

    private val searchTopicsUseCase: SearchTopicsUseCase = SearchTopicsUseCaseImpl()
    private val topicToItemMapper: TopicToItemMapper = TopicToItemMapper()
    private val topicEntityToItemMapper: TopicsEntityToItemMapper = TopicsEntityToItemMapper()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val searchTopicSubject: PublishSubject<Long> = PublishSubject.create()
    private val searchDBTopicSubject: PublishSubject<Long> = PublishSubject.create()
    private val topicsRepository: TopicRepository =
        TopicRepositoryImpl(App.instance!!.database!!.topicDao())

    init {
        subscribeToTopicSearchChanges()
        subscribeToTopicSearchDBChanges()
    }

    fun searchTopicsDB(searchQuery: Long) {
        searchDBTopicSubject.onNext(searchQuery)
    }

    fun insertTopics(topics: List<StreamTopicItem>) {
        topicsRepository.insertAll(topics.map { Topic(it.id, it.parentId, it.name) })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {},
                onError = { _streamsScreenState.value = StreamsScreenState.Error(it) }
            )
    }

    private fun subscribeToTopicSearchChanges() {
        searchTopicSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .doOnNext { _streamsScreenState.postValue(StreamsScreenState.Loading) }
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMapSingle { searchQuery -> searchTopicsUseCase(searchQuery) }
            .map(topicToItemMapper)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _streamsScreenState.value = StreamsScreenState.Result(it) },
                onError = { _streamsScreenState.value = StreamsScreenState.Error(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun subscribeToTopicSearchDBChanges() {
        searchDBTopicSubject
            .subscribeOn(Schedulers.io())
            .doOnNext { _streamsDBScreenState.postValue(StreamsScreenState.Loading) }
            .debounce(10, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMapMaybe { searchQuery ->
                topicsRepository.loadAllByStream(searchQuery).map { topics ->
                    if (topics.isNotEmpty()) {
                        topics
                    } else {
                        searchTopicSubject.onNext(searchQuery)
                        topics
                    }
                }
            }
            .map(topicEntityToItemMapper)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _streamsDBScreenState.value = StreamsScreenState.Result(it) },
                onError = { _streamsDBScreenState.value = StreamsScreenState.Error(it) }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
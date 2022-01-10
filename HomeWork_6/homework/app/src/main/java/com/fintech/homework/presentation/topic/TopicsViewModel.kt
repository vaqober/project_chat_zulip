package com.fintech.homework.presentation.topic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fintech.homework.network.usecase.search.SearchTopicsUseCase
import com.fintech.homework.network.usecase.search.SearchTopicsUseCaseImpl
import com.fintech.homework.presentation.streams.StreamsScreenState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

internal class TopicsViewModel : ViewModel() {

    private val _Streams_screenState: MutableLiveData<StreamsScreenState> = MutableLiveData()
    val streamsScreenState: LiveData<StreamsScreenState>
        get() = _Streams_screenState

    private val searchTopicsUseCase: SearchTopicsUseCase = SearchTopicsUseCaseImpl()
    private val topicToItemMapper: TopicToItemMapper = TopicToItemMapper()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val searchTopicSubject: PublishSubject<Long> = PublishSubject.create()

    init {
        subscribeToTopicSearchChanges()
    }

    fun searchTopics(searchQuery: Long) {
        searchTopicSubject.onNext(searchQuery)
    }

    private fun subscribeToTopicSearchChanges() {
        searchTopicSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .doOnNext { _Streams_screenState.postValue(StreamsScreenState.Loading) }
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMapSingle { searchQuery -> searchTopicsUseCase(searchQuery) }
            .map(topicToItemMapper)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _Streams_screenState.value = StreamsScreenState.Result(it) },
                onError = { _Streams_screenState.value = StreamsScreenState.Error(it) }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
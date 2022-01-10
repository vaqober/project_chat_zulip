package com.fintech.homework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fintech.homework.adapters.TopicToItemMapper
import com.fintech.homework.data.ScreenState
import com.fintech.homework.usecase.SearchTopicsUseCase
import com.fintech.homework.usecase.SearchTopicsUseCaseImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

internal class TopicsViewModel : ViewModel() {

    private val _screenState: MutableLiveData<ScreenState> = MutableLiveData()
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val searchTopicsUseCase: SearchTopicsUseCase = SearchTopicsUseCaseImpl()
    private val topicToItemMapper: TopicToItemMapper = TopicToItemMapper()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val searchTopicSubject: PublishSubject<Int> = PublishSubject.create()

    init {
        subscribeToTopicSearchChanges()
    }

    fun searchTopics(searchQuery: Int) {
        searchTopicSubject.onNext(searchQuery)
    }

    private fun subscribeToTopicSearchChanges() {
        searchTopicSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .doOnNext { _screenState.postValue(ScreenState.Loading) }
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMap { searchQuery -> searchTopicsUseCase(searchQuery) }
            .map(topicToItemMapper)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _screenState.value = ScreenState.Result(it) },
                onError = { _screenState.value = ScreenState.Error(it) }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        const val INITIAL_QUERY: String = ""
    }
}
package com.fintech.homework.presentation.streams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fintech.homework.network.usecase.search.SearchStreamsUseCase
import com.fintech.homework.network.usecase.search.SearchStreamsUseCaseImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

internal class StreamsViewModel : ViewModel() {

    private val _streamsScreenState: MutableLiveData<StreamsScreenState> = MutableLiveData()
    val streamsScreenState: LiveData<StreamsScreenState>
        get() = _streamsScreenState

    private val searchStreamsUseCase: SearchStreamsUseCase = SearchStreamsUseCaseImpl()
    private val streamsToItemMapper: StreamsToItemMapper = StreamsToItemMapper()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val searchStreamSubject: PublishSubject<String> = PublishSubject.create()

    init {
        subscribeToStreamSearchChanges()
    }

    fun searchStreams(searchQuery: String) {
        searchStreamSubject.onNext(searchQuery)
    }

    private fun subscribeToStreamSearchChanges() {
        searchStreamSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .doOnNext { _streamsScreenState.postValue(StreamsScreenState.Loading) }
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMapSingle { searchQuery -> searchStreamsUseCase(searchQuery) }
            .map(streamsToItemMapper)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _streamsScreenState.value = StreamsScreenState.Result(it) },
                onError = { _streamsScreenState.value = StreamsScreenState.Error(it) }
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
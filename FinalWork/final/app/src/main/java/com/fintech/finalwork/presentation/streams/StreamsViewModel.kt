package com.fintech.finalwork.presentation.streams

import androidx.lifecycle.ViewModel
import com.fintech.finalwork.presentation.streams.elm.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import vivid.money.elmslie.core.store.Store
import java.util.concurrent.TimeUnit

internal class StreamsViewModel : ViewModel() {

    lateinit var store: Store<Event, Effect, State>

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val searchStreamSubject: PublishSubject<SearchQuery> = PublishSubject.create()
    private val subscribeStreamSubject: PublishSubject<Subscribe> = PublishSubject.create()

    init {
        subscribeToStreamSearchChanges()
        subscribeToStreamSubscribeChanges()
    }

    fun searchStreamsInDB(searchQuery: SearchQuery) {
        searchStreamSubject.onNext(searchQuery)
    }

    fun subscribeStream(subscribe: Subscribe) {
        subscribeStreamSubject.onNext(subscribe)
    }

    private fun subscribeToStreamSearchChanges() {
        searchStreamSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { searchQuery ->
                store.accept(
                    Event.Ui.LoadStreams(
                        searchQuery
                    )
                )
            }
            .subscribe()
            .addTo(compositeDisposable)
    }

    private fun subscribeToStreamSubscribeChanges() {
        subscribeStreamSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { subscribeQuery ->
                store.accept(
                    Event.Ui.SubscribeStream(
                        subscribeQuery
                    )
                )
            }
            .subscribe()
            .addTo(compositeDisposable)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
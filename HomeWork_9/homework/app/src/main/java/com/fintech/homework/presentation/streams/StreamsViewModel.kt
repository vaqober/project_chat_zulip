package com.fintech.homework.presentation.streams

import androidx.lifecycle.ViewModel
import com.fintech.homework.presentation.streams.elm.Effect
import com.fintech.homework.presentation.streams.elm.Event
import com.fintech.homework.presentation.streams.elm.SearchQuery
import com.fintech.homework.presentation.streams.elm.State
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

    init {
        subscribeToStreamSearchChanges()
        subscribeToStreamSearchChanges()
    }

    fun searchStreamsInDB(searchQuery: SearchQuery) {
        searchStreamSubject.onNext(searchQuery)
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


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        const val INITIAL_QUERY: String = ""
    }
}
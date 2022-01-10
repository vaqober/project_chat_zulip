package com.fintech.homework.presentation.streams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fintech.homework.App
import com.fintech.homework.database.objects.entity.Stream
import com.fintech.homework.database.objects.repository.StreamsRepository
import com.fintech.homework.database.objects.repository.StreamsRepositoryImpl
import com.fintech.homework.network.usecase.search.SearchStreamsUseCase
import com.fintech.homework.network.usecase.search.SearchStreamsUseCaseImpl
import com.fintech.homework.presentation.streams.objects.StreamTopicItem
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
    private val _streamsDBScreenState: MutableLiveData<StreamsScreenState> = MutableLiveData()
    val streamsDBScreenState: LiveData<StreamsScreenState>
        get() = _streamsDBScreenState

    private val searchStreamsUseCase: SearchStreamsUseCase = SearchStreamsUseCaseImpl()
    private val streamsToItemMapper: StreamsToItemMapper = StreamsToItemMapper()
    private val streamsEntityToItemMapper: StreamsEntityToItemMapper = StreamsEntityToItemMapper()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val searchStreamSubject: PublishSubject<String> = PublishSubject.create()
    private val searchStreamSubjectDB: PublishSubject<String> = PublishSubject.create()
    private val streamsRepository: StreamsRepository =
        StreamsRepositoryImpl(App.instance!!.database!!.streamDao())


    init {
        subscribeToStreamSearchChanges()
        subscribeToStreamSearchInDatabase()
    }

    fun searchStreamsInDB(searchQuery: String) {
        searchStreamSubjectDB.onNext(searchQuery)
    }

    fun insertStreamsInDB(streams: List<StreamTopicItem>) {
        streamsRepository.insertAll(streams.map { Stream(it.id, it.name, true) })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {},
                onError = { _streamsScreenState.value = StreamsScreenState.Error(it) }
            )
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

    private fun subscribeToStreamSearchInDatabase() {
        searchStreamSubjectDB
            .subscribeOn(Schedulers.io())
            .doOnNext { _streamsDBScreenState.postValue(StreamsScreenState.Loading) }
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMapMaybe { searchQuery ->
                streamsRepository.findByName(searchQuery).map {
                    if (it.isNotEmpty()) {
                        it
                    } else {
                        searchStreamSubject.onNext(searchQuery)
                        it
                    }
                }
            }
            .map(streamsEntityToItemMapper)
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

    companion object {
        const val INITIAL_QUERY: String = ""
    }
}
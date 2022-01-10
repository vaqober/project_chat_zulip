package com.fintech.homework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fintech.homework.adapters.ChannelToItemMapper
import com.fintech.homework.data.ScreenState
import com.fintech.homework.usecase.SearchChannelsUseCase
import com.fintech.homework.usecase.SearchChannelsUseCaseImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

internal class ChannelsViewModel : ViewModel() {

    private val _screenState: MutableLiveData<ScreenState> = MutableLiveData()
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val searchChannelsUseCase: SearchChannelsUseCase = SearchChannelsUseCaseImpl()
    private val channelToItemMapper: ChannelToItemMapper = ChannelToItemMapper()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val searchChannelSubject: PublishSubject<String> = PublishSubject.create()

    init {
        subscribeToChannelSearchChanges()
    }

    fun searchChannels(searchQuery: String) {
        searchChannelSubject.onNext(searchQuery)
    }

    private fun subscribeToChannelSearchChanges() {
        searchChannelSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .doOnNext { _screenState.postValue(ScreenState.Loading) }
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMap { searchQuery -> searchChannelsUseCase(searchQuery) }
            .map(channelToItemMapper)
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
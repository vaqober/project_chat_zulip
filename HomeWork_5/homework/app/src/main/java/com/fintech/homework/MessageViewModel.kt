package com.fintech.homework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fintech.homework.data.MessageScreenState
import com.fintech.homework.usecase.SearchMessageUseCase
import com.fintech.homework.usecase.SearchMessagesUseCaseImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

internal class MessageViewModel : ViewModel() {

    private val _messageScreenState: MutableLiveData<MessageScreenState> = MutableLiveData()
    val messageScreenState: LiveData<MessageScreenState>
        get() = _messageScreenState

    private val searchMessagesUseCase: SearchMessageUseCase = SearchMessagesUseCaseImpl()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val searchMessageSubject: PublishSubject<String> = PublishSubject.create()

    init {
        subscribeToMessageSearchChanges()
    }

    fun searchMessages(searchQuery: String) {
        searchMessageSubject.onNext(searchQuery)
    }

    private fun subscribeToMessageSearchChanges() {
        searchMessageSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .doOnNext { _messageScreenState.postValue(MessageScreenState.Loading) }
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMap { searchQuery -> searchMessagesUseCase(searchQuery) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _messageScreenState.value = MessageScreenState.Result(it) },
                onError = { _messageScreenState.value = MessageScreenState.Error(it) }
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
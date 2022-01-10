package com.fintech.homework.presentation.topic.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fintech.homework.presentation.RequestsBlankScreenState
import com.fintech.homework.presentation.RequestsIdScreenState
import com.fintech.homework.presentation.topic.domain.query.MessageQuery
import com.fintech.homework.presentation.topic.domain.query.ReactionQuery
import com.fintech.homework.presentation.topic.domain.query.SearchQuery
import com.fintech.homework.presentation.topic.elm.Effect
import com.fintech.homework.presentation.topic.elm.Event
import com.fintech.homework.presentation.topic.elm.State
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import vivid.money.elmslie.core.store.Store
import java.util.concurrent.TimeUnit

internal class MessageViewModel : ViewModel() {

    lateinit var store: Store<Event, Effect, State>

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val _messageScreenState: MutableLiveData<MessageScreenState> = MutableLiveData()
    private val _requestScreenState: MutableLiveData<RequestsBlankScreenState> = MutableLiveData()
    private val _requestIdScreenState: MutableLiveData<RequestsIdScreenState> = MutableLiveData()
    val messageScreenState: LiveData<MessageScreenState>
        get() = _messageScreenState
    private val _messageDBScreenState: MutableLiveData<MessageScreenState> = MutableLiveData()
    val messageDBScreenState: LiveData<MessageScreenState>
        get() = _messageDBScreenState
    val requestScreenState: LiveData<RequestsBlankScreenState>
        get() = _requestScreenState
    val requestIdScreenState: LiveData<RequestsIdScreenState>
        get() = _requestIdScreenState

    private val searchMessageSubject: PublishSubject<SearchQuery> = PublishSubject.create()
    private val postMessageSubject: PublishSubject<MessageQuery> = PublishSubject.create()
    private val postReactionSubject: PublishSubject<ReactionQuery> = PublishSubject.create()
    private val deleteReactionSubject: PublishSubject<ReactionQuery> = PublishSubject.create()

    init {
        subscribeToMessageSearchChanges()
        subscribeToMessagePostChanges()
        subscribeToReactionPostChanges()
        subscribeToReactionDeleteChanges()
    }

    fun searchMessages(searchQuery: SearchQuery) {
        searchMessageSubject.onNext(searchQuery)
    }

    fun subscribeToMessagesDB(stream: Long, topic: String) {
        store.accept(Event.Ui.SubscribeToDBMessagesLoading(stream, topic))
    }

    fun postMessage(messageQuery: MessageQuery) {
        postMessageSubject.onNext(messageQuery)
    }

    fun postReaction(emoji_name: String, message_id: Long) {
        postReactionSubject.onNext(
            ReactionQuery(
                emoji_name,
                "unicode_emoji",
                message_id
            )
        )
    }

    fun deleteReaction(emoji_name: String, message_id: Long) {
        deleteReactionSubject.onNext(
            ReactionQuery(
                emoji_name,
                "unicode_emoji",
                message_id
            )
        )
    }

    private fun subscribeToMessageSearchChanges() {
        searchMessageSubject
            .subscribeOn(Schedulers.io())
            .debounce(1000, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { searchQuery -> store.accept(Event.Ui.LoadMessages(searchQuery)) }
            .subscribe()
            .addTo(compositeDisposable)
    }

    private fun subscribeToMessagePostChanges() {
        postMessageSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { messageQuery -> store.accept(Event.Ui.PostMessage(messageQuery)) }
            .subscribe()
            .addTo(compositeDisposable)
    }

    private fun subscribeToReactionPostChanges() {
        postReactionSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { reactionQuery -> store.accept(Event.Ui.PostReaction(reactionQuery)) }
            .subscribe()
            .addTo(compositeDisposable)
    }

    private fun subscribeToReactionDeleteChanges() {
        deleteReactionSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { reactionQuery -> store.accept(Event.Ui.DeleteReaction(reactionQuery)) }
            .subscribe()
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
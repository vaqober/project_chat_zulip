package com.fintech.homework.presentation.topic.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fintech.homework.App
import com.fintech.homework.database.objects.entity.MessageEntity
import com.fintech.homework.database.objects.repository.MessageRepository
import com.fintech.homework.database.objects.repository.MessageRepositoryImpl
import com.fintech.homework.network.data.models.query.Action
import com.fintech.homework.network.data.models.query.MessageQuery
import com.fintech.homework.network.data.models.query.ReactionQuery
import com.fintech.homework.network.data.models.query.SearchQuery
import com.fintech.homework.network.usecase.ReactionUseCase
import com.fintech.homework.network.usecase.delete.ReactionUseCaseImpl
import com.fintech.homework.network.usecase.post.PostMessageUseCase
import com.fintech.homework.network.usecase.post.PostMessageUseCaseImpl
import com.fintech.homework.network.usecase.search.SearchMessageUseCase
import com.fintech.homework.network.usecase.search.SearchMessagesUseCaseImpl
import com.fintech.homework.presentation.RequestsBlankScreenState
import com.fintech.homework.presentation.RequestsIdScreenState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

internal class MessageViewModel : ViewModel() {

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

    private val searchMessagesUseCase: SearchMessageUseCase = SearchMessagesUseCaseImpl()
    private val postMessageUseCase: PostMessageUseCase = PostMessageUseCaseImpl()
    private val reactionUseCase: ReactionUseCase = ReactionUseCaseImpl()
    private val messageRepository: MessageRepository =
        MessageRepositoryImpl(App.instance!!.database!!.messageDao())
    private val messageInfoToItemMapper: MessageInfoToItemMapper = MessageInfoToItemMapper()
    private val messageEntityToItemMapper: MessageEntityToItemMapper = MessageEntityToItemMapper()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val searchMessageSubject: PublishSubject<SearchQuery> = PublishSubject.create()
    private val postMessageSubject: PublishSubject<MessageQuery> = PublishSubject.create()
    private val reactionSubject: PublishSubject<ReactionQuery> = PublishSubject.create()

    init {
        subscribeToMessageSearchChanges()
        subscribeToMessagePostChanges()
        subscribeToReactionChanges()
    }

    fun searchMessages(searchQuery: SearchQuery) {
        searchMessageSubject.onNext(searchQuery)
    }

    fun subscribeToMessagesDB(stream: Long, topic: String) {
        subscribeToDBMessageSearchChanges(stream, topic)
    }

    fun insertMessagesDB(messages: List<Message>) {
        //Save only last 50 elements
        val lastMessages = if (messages.size <= 50) {
            messages
        } else {
            messages.subList(messages.size - 50, messages.size - 1)
        }
        messageRepository.insertAll(lastMessages.map {
            MessageEntity(
                it.id,
                it.stream_id,
                it.topic,
                it.sender_id,
                it.sender_name,
                it.content,
                it.timestamp,
                it.reactions.toList()
            )
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {},
                onError = { _messageDBScreenState.value = MessageScreenState.Error(it) }
            )
    }

    fun postMessage(messageQuery: MessageQuery) {
        postMessageSubject.onNext(messageQuery)
    }

    fun postReaction(emoji_name: String, message_id: Long) {
        reactionSubject.onNext(
            ReactionQuery(
                emoji_name,
                "unicode_emoji",
                message_id,
                Action.POST
            )
        )
    }

    fun deleteReaction(emoji_name: String, message_id: Long) {
        reactionSubject.onNext(
            ReactionQuery(
                emoji_name,
                "unicode_emoji",
                message_id,
                Action.DELETE
            )
        )
    }

    private fun subscribeToMessageSearchChanges() {
        searchMessageSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .doOnNext { _messageScreenState.postValue(MessageScreenState.Loading) }
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMapSingle { searchQuery -> searchMessagesUseCase(searchQuery) }
            .map(messageInfoToItemMapper)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _messageScreenState.value = MessageScreenState.Result(it) },
                onError = { _messageScreenState.value = MessageScreenState.Error(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun subscribeToDBMessageSearchChanges(stream: Long, topic: String) {
        messageRepository.loadAllByStreamTopic(
            stream,
            topic
        )
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .debounce(100, TimeUnit.MILLISECONDS, Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(messageEntityToItemMapper)
            .subscribeBy(
                onNext = { _messageDBScreenState.value = MessageScreenState.Result(it) },
                onError = { _messageDBScreenState.value = MessageScreenState.Error(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun subscribeToMessagePostChanges() {
        postMessageSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .doOnNext { _requestIdScreenState.postValue(RequestsIdScreenState.Loading) }
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMapSingle { messageQuery -> postMessageUseCase(messageQuery) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _requestIdScreenState.value = RequestsIdScreenState.Result(it) },
                onError = { _requestIdScreenState.value = RequestsIdScreenState.Error(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun subscribeToReactionChanges() {
        reactionSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .doOnNext { _requestScreenState.postValue(RequestsBlankScreenState.Loading) }
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMapSingle { reactionQuery -> reactionUseCase(reactionQuery) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _requestScreenState.value = RequestsBlankScreenState.Result(it) },
                onError = { _requestScreenState.value = RequestsBlankScreenState.Error(it) }
            )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        const val INITIAL_QUERY: String = ""
        const val PAGING_COUNT: Int = 25
        const val INITIAL_TYPES: String = "newest"
    }
}
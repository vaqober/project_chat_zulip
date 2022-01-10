package com.fintech.homework.presentation.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fintech.homework.network.usecase.search.SearchPeopleUseCase
import com.fintech.homework.network.usecase.search.SearchPeopleUseCaseImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

internal class PeopleViewModel : ViewModel() {
    private val _peopleScreenState: MutableLiveData<PeopleScreenState> = MutableLiveData()
    val peopleScreenState: LiveData<PeopleScreenState>
        get() = _peopleScreenState

    private val searchPeopleUseCase: SearchPeopleUseCase = SearchPeopleUseCaseImpl()
    private val peoplesToItemMapper: PersonInfoToItemMapper = PersonInfoToItemMapper()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val searchPeopleSubject: PublishSubject<String> = PublishSubject.create()

    init {
        subscribeToPeopleSearchChanges()
    }

    fun searchPeople(searchQuery: String) {
        searchPeopleSubject.onNext(searchQuery)
    }

    private fun subscribeToPeopleSearchChanges() {
        searchPeopleSubject
            .subscribeOn(Schedulers.io())
            .distinctUntilChanged()
            .doOnNext { _peopleScreenState.postValue(PeopleScreenState.Loading) }
            .debounce(500, TimeUnit.MILLISECONDS, Schedulers.io())
            .switchMapSingle { searchQuery -> searchPeopleUseCase(searchQuery) }
            .map(peoplesToItemMapper)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _peopleScreenState.value = PeopleScreenState.Result(it) },
                onError = { _peopleScreenState.value = PeopleScreenState.Error(it) }
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
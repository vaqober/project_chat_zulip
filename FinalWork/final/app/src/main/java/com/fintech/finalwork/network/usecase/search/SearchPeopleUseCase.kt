package com.fintech.finalwork.network.usecase.search

import com.fintech.finalwork.network.RetrofitClient
import com.fintech.finalwork.network.data.models.PersonInfo
import com.fintech.finalwork.network.services.PeopleService
import io.reactivex.rxjava3.core.Single

interface SearchPeopleUseCase : (String) -> Single<List<PersonInfo>> {
    override fun invoke(searchQuery: String): Single<List<PersonInfo>>
}

internal class SearchPeopleUseCaseImpl : SearchPeopleUseCase {
    private val retroClient = RetrofitClient.retrofit.create(PeopleService::class.java)

    override fun invoke(searchQuery: String): Single<List<PersonInfo>> {
        return retroClient.getAllPeople()
            .map {
                it.members.filter { member ->
                    member.fullName.contains(
                        searchQuery,
                        ignoreCase = true
                    )
                }
            }
    }
}
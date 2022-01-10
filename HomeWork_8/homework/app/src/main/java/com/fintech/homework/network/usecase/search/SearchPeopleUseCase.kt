package com.fintech.homework.network.usecase.search

import com.fintech.homework.network.RetrofitClient
import com.fintech.homework.network.data.models.PersonInfo
import com.fintech.homework.network.services.PeopleService
import io.reactivex.rxjava3.core.Single

interface SearchPeopleUseCase : (String) -> Single<List<PersonInfo>> {
    override fun invoke(searchQuery: String): Single<List<PersonInfo>>
}

internal class SearchPeopleUseCaseImpl : SearchPeopleUseCase {
    private val retroClient = RetrofitClient.retrofit.create(PeopleService::class.java)

    override fun invoke(searchQuery: String): Single<List<PersonInfo>> {
        return retroClient.getAllPeople()
            .map { it.members.filter { member -> member.full_name.contains(searchQuery, ignoreCase = true) } }
    }
}
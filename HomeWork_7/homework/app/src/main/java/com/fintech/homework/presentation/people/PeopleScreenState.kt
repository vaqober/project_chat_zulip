package com.fintech.homework.presentation.people

internal sealed class PeopleScreenState {

    class Result(val items: List<PersonItem>) : PeopleScreenState()

    object Loading : PeopleScreenState()

    class Error(val error: Throwable) : PeopleScreenState()
}
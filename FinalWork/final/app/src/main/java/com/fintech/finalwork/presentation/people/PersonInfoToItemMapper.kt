package com.fintech.finalwork.presentation.people

import com.fintech.finalwork.network.data.models.PersonInfo

internal class PersonInfoToItemMapper : (List<PersonInfo>) -> List<PersonItem> {
    override fun invoke(people: List<PersonInfo>): List<PersonItem> {
        return people.map { person ->
            PersonItem(person.userId, person.fullName, person.email, person.avatarUrl)
        }
    }
}
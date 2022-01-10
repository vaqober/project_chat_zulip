package com.fintech.homework.presentation.people

import com.fintech.homework.network.data.models.PersonInfo

internal class PersonInfoToItemMapper: (List<PersonInfo>) -> List<PersonItem> {
    override fun invoke(people: List<PersonInfo>): List<PersonItem> {
        return people.map { person ->
            PersonItem(person.user_id, person.full_name, person.email)
        }
    }
}
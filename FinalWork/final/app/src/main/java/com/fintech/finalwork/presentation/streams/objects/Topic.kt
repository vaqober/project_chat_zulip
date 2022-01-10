package com.fintech.finalwork.presentation.streams.objects

data class Topic(val parent: Long, val name: String) {
    override fun toString(): String {
        return "$parent,$name"
    }
}
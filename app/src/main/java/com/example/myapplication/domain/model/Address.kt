package com.example.myapplication.domain.model

import androidx.annotation.Keep

@Keep
data class Address(
    val city: String,
    val neighborhood: String,
    val street: String,
    val number: String,
    val userComplements: Map<String, String> = emptyMap()
) {
    constructor() : this(
        "", "", "", "", emptyMap()
    )

    fun getComplementForUser(userId: String): String {
        return userComplements[userId] ?: ""
    }
}
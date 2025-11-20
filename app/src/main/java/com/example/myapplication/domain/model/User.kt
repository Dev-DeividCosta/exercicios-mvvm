package com.example.myapplication.domain.model

import androidx.annotation.Keep

@Keep
data class User(
    val id: String,
    val name: String,
    val role: String
) {
    constructor() : this("", "", "")
}
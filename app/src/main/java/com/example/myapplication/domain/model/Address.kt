package com.example.myapplication.domain.model

data class Address(
    val city: String,
    val neighborhood: String,
    val street: String,
    val houseNumber: String,
    val complement: String
) {
    constructor() : this(
        "", "", "", "", ""
    )
}

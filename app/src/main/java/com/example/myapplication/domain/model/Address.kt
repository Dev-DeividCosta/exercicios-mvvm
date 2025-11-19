package com.example.myapplication.domain.model

data class Address(
    val zipCode: String = "",
    val city: String = "",
    val state: String = "",
    val neighborhood: String = "",
    val street: String = "",
    val houseNumber: String = "",
    val complement: String = ""
) {
    constructor() : this("", "", "", "", "", "", "")
}

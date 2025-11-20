package com.example.myapplication.domain.model

import androidx.annotation.Keep

@Keep
data class Client(
    val id: String,
    val name: String,
    val cpf: String,
    val phone: String,
    val address: Address
) {
    constructor() : this(
        "", "", "", "", Address()
    )
}

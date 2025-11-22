package com.example.myapplication.domain.model

import androidx.annotation.Keep

@Keep
data class Client(
    val id: String,
    val name: String,
    val cpf: String,
    val phone: String,
    val address: Address,
    val routeIndex: Int = 0
) {
    constructor() : this(
        "", "", "", "", Address(), 0
    )
}

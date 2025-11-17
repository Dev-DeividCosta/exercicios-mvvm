package com.example.myapplication.domain.model

data class Sale(
    val id: String,
    val clientName: String,
    val productName: String,
    val installments: Int,
    val observation: String
) {
    constructor() : this(
        "", "", "", 0, ""
    )
}

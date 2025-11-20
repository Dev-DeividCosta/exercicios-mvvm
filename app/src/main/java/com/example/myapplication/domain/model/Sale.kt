package com.example.myapplication.domain.model

data class Sale(
    val id: String,
    val clientId: String,
    val clientName: String,
    val productId: String,
    val productName: String,
    val quantity: Int,
    val installments: Int,
    val totalValue: Double,
    val observation: String,
    val date: Long,
    val deviceId: String
) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        1,
        1,
        0.0,
        "",
        System.currentTimeMillis(),
        ""
    )
}
package com.example.myapplication.domain.model

import androidx.annotation.Keep

@Keep
data class Sale(
    val id: String,
    val clientId: String,
    val clientName: String,
    val items: List<SaleItem>,
    val installments: Int,
    val totalValue: Double,
    val observation: String,
    val date: Long,
    val sellerDeviceId: String
) {
    constructor() : this(
        "",
        "",
        "",
        emptyList(),
        1,
        0.0,
        "",
        System.currentTimeMillis(),
        ""
    )
}
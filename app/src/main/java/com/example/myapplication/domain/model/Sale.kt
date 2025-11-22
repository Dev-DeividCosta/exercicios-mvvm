package com.example.myapplication.domain.model

import androidx.annotation.Keep

@Keep
data class Sale(
    val id: String,
    val clientId: String,
    val clientName: String,
    val items: List<SaleItem>,
    val installments: List<Installment>,
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
        emptyList(),
        0.0,
        "",
        System.currentTimeMillis(),
        ""
    )
}
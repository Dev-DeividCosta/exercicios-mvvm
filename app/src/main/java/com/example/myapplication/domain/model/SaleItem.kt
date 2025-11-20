package com.example.myapplication.domain.model

data class SaleItem(
    val productId: String,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double
) {
    constructor() : this("", "", 1, 0.0)

    val subtotal: Double
        get() = quantity * unitPrice
}
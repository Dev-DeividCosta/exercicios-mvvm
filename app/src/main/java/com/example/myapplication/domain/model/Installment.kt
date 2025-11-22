package com.example.myapplication.domain.model

import androidx.annotation.Keep

@Keep
data class Installment(
    val id: String,
    val saleId: String,
    val clientId: String,
    val number: Int,
    val dueDate: Long,
    val originalValue: Double,
    val amountPaid: Double = 0.0,
    val paymentDate: Long? = null
) {
    constructor() : this(
        "", "", "", 0, 0L, 0.0, 0.0, null
    )

    val remainingAmount: Double
        get() = (originalValue - amountPaid).coerceAtLeast(0.0)

    val isPaid: Boolean
        get() = remainingAmount <= 0.001
}
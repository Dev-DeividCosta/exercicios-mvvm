package com.example.myapplication.domain.model

import androidx.annotation.Keep

@Keep
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val description: String,
    val stockQuantity: Int
) {
    constructor() : this(
        "", "", 0.0, "", 0
    )
}
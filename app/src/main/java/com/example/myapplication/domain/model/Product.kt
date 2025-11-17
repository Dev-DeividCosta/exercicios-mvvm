package com.example.myapplication.domain.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val stock: Int
) {
    constructor() : this(
        "", "", 0.0, 0
    )
}
package com.example.myapplication.domain.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val description: String,
    val imageUrl: String
) {
    constructor() : this(
        "", "", 0.0, "", ""
    )
}
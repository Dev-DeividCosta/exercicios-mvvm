package com.example.myapplication.domain.model

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val descricao: String,
    val urlImagem: String
) {
    constructor() : this(
        "", "", 0.0, "", ""
    )
}
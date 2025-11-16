package com.example.myapplication.domain.model

data class Produto1(
    val id: String,
    val nome: String,
    val preco: Double,
    val descricao: String,
    val urlImagem: String
) {
    constructor() : this("", "", 0.0, "", "")
}
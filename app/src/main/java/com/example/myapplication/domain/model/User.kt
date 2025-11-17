package com.example.myapplication.domain.model

// Bônus: Para gerenciar Cobradores e Vendedores
data class User(
    val id: String,
    val name: String,
    val role: String // "VENDEDOR", "COBRADOR", "ADMIN"
) {
    // Construtor vazio para o Firestore
    constructor() : this("", "", "")
}
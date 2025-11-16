package com.example.myapplication.domain.model

// As respostas do checklist viraram este Model:
data class Produto(
    // Identidade -> Produto

    // Propriedades e Tipos de Dados
    val id: String,
    val nome: String,
    val preco: Double,
    val descricao: String,
    val urlImagem: String
) {
    // Construtor vazio (baseado nas Restrições/Padrões)
    // O Firestore precisa disso!
    constructor() : this(
        id = "",
        nome = "",
        preco = 0.0,
        descricao = "",
        urlImagem = ""
    )
}

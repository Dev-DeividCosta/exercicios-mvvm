// File: app/src/main/java/com/example/myapplication/ui/viewmodel/ProductsUiState.kt

package com.example.myapplication.ui.viewmodel

import com.example.myapplication.domain.model.Product

/**
 * Representa o estado da interface do usuário da tela de Produtos.
 * O uso de uma classe selada garante que o estado da UI seja sempre um destes três.
 */
sealed class ProductsUiState {
    /** Estado inicial ou quando a lista está sendo carregada. */
    data object Loading : ProductsUiState()

    /** Estado de sucesso com a lista de produtos carregada. */
    data class Success(val products: List<Product>) : ProductsUiState()

    /** Estado de falha ao carregar os produtos. */
    data class Error(val message: String) : ProductsUiState()
}
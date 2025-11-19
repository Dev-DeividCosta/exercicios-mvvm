// File: app/src/main/java/com/example/myapplication/ui/viewmodel/ProductViewModel.kt

package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel // Necessário se estiver usando Hilt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para a tela de Produtos.
 * Gerencia o estado da lista de produtos e a comunicação com a camada de domínio.
 *
 * @property getProductsUseCase O Use Case responsável por buscar os produtos.
 */
@HiltViewModel // Adicione esta anotação se estiver usando Hilt
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    // MutableStateFlow que armazena o estado atual da UI
    private val _uiState = MutableStateFlow<ProductsUiState>(ProductsUiState.Loading)

    /**
     * StateFlow público que a Composable Screen irá coletar.
     */
    val uiState: StateFlow<ProductsUiState> = _uiState

    init {
        // Inicia a busca pelos produtos assim que o ViewModel é criado
        loadProducts()
    }

    /**
     * Inicia o fluxo de coleta de dados de produtos do Use Case.
     */
    fun loadProducts() {
        viewModelScope.launch {
            getProductsUseCase()
                // 1. Antes de iniciar, emite o estado de Carregamento.
                .onStart { _uiState.value = ProductsUiState.Loading }
                // 2. Mapeia a lista de Products (sucesso) para o estado de Sucesso da UI.
                .map { productsList -> ProductsUiState.Success(productsList) }
                // 3. Captura qualquer exceção (ex: falha no Firestore) e mapeia para o estado de Erro.
                .catch { exception ->
                    val message = "Erro ao carregar produtos: ${exception.localizedMessage ?: "Desconhecido"}"
                    _uiState.value = ProductsUiState.Error(message)
                }
                // 4. Coleta o resultado final e atualiza o StateFlow.
                .collect { uiStateResult ->
                    _uiState.value = uiStateResult
                }
        }
    }
}
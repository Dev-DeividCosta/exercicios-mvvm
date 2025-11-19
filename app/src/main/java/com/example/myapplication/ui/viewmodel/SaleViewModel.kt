package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecase.CreateSaleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.lang.IllegalArgumentException
import java.util.NoSuchElementException

// 1. Definição do Estado da UI (para a tela saber o que mostrar)
sealed interface SaleUiState {
    data object Idle : SaleUiState
    data object Loading : SaleUiState
    data object Success : SaleUiState
    data class Error(val message: String) : SaleUiState
}

@HiltViewModel
class SaleViewModel @Inject constructor(
    // 2. Injetando o Caso de Uso de Venda
    private val createSaleUseCase: CreateSaleUseCase
    // Nota: Se precisarmos de uma lista de produtos, injetamos o ProductRepository aqui
    // mas para o save, apenas o UseCase basta.
) : ViewModel() {

    private val _uiState = MutableStateFlow<SaleUiState>(SaleUiState.Idle)
    val uiState: StateFlow<SaleUiState> = _uiState.asStateFlow()

    fun saveSale(
        // Os nomes são usados temporariamente, mas vamos precisar do ID do Produto
        selectedProductId: String,
        clientName: String,
        quantity: Int = 1, // Por enquanto, fixo em 1
        installments: Int,
        observation: String
    ) {
        if (selectedProductId.isBlank()) {
            _uiState.value = SaleUiState.Error("Selecione um produto antes de salvar.")
            return
        }

        viewModelScope.launch {
            _uiState.value = SaleUiState.Loading

            try {
                // 3. Executando a lógica de negócio (Use Case)
                createSaleUseCase.execute(
                    selectedProductId = selectedProductId,
                    clientName = clientName,
                    quantity = quantity,
                    installments = installments,
                    observation = observation
                )

                _uiState.value = SaleUiState.Success

            } catch (e: NoSuchElementException) {
                // 4. Tratamento de Erro de Lógica (Produto não existe)
                _uiState.value = SaleUiState.Error(e.message ?: "Produto não encontrado durante a venda.")
            } catch (e: IllegalArgumentException) {
                // 4. Tratamento de Erro de Argumento (ID inválido)
                _uiState.value = SaleUiState.Error(e.message ?: "Dados inválidos.")
            } catch (e: Exception) {
                // 4. Tratamento de Erro Geral (Rede/Firebase)
                _uiState.value = SaleUiState.Error(e.localizedMessage ?: "Falha desconhecida ao salvar a venda.")
            }
        }
    }

    /**
     * Reseta o estado da UI para 'Idle' (para fechar a Snackbar, por exemplo).
     */
    fun resetUiState() {
        _uiState.value = SaleUiState.Idle
    }
}
package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Client
import com.example.myapplication.domain.model.Product
import com.example.myapplication.domain.model.Sale
import com.example.myapplication.domain.model.SaleItem
import com.example.myapplication.domain.repository.ClientRepository
import com.example.myapplication.domain.repository.ProductRepository
import com.example.myapplication.domain.usecase.CreateSaleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ----------------------------------------------------
// MODELO DE ESTADO DA UI (SALE)
// ----------------------------------------------------
data class SaleUiState(
    val currentSale: Sale = Sale(), // A venda sendo construída
    val availableProducts: List<Product> = emptyList(), // Lista para o modal de seleção
    val availableClients: List<Client> = emptyList(), // Lista para o modal de seleção
    val isLoading: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null
)

// ----------------------------------------------------
// VIEW MODEL
// ----------------------------------------------------
@HiltViewModel
class SaleViewModel @Inject constructor(
    private val createSaleUseCase: CreateSaleUseCase,
    private val productRepository: ProductRepository, // Para listar produtos na tela
    private val clientRepository: ClientRepository    // Para listar clientes na tela
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaleUiState())
    val uiState: StateFlow<SaleUiState> = _uiState.asStateFlow()

    init {
        // Carrega as listas necessárias para fazer uma venda
        loadDependencies()
    }

    private fun loadDependencies() {
        viewModelScope.launch {
            // Carrega produtos
            launch {
                productRepository.getAllProducts().collect { products ->
                    _uiState.update { it.copy(availableProducts = products) }
                }
            }
            // Carrega clientes
            launch {
                clientRepository.getAllClients().collect { clients ->
                    _uiState.update { it.copy(availableClients = clients) }
                }
            }
        }
    }

    /**
     * Define o cliente da venda.
     */
    fun selectClient(client: Client) {
        _uiState.update { state ->
            state.copy(
                currentSale = state.currentSale.copy(
                    clientId = client.id,
                    clientName = client.name
                )
            )
        }
    }

    /**
     * Adiciona um produto à lista de itens da venda.
     * Recalcula o total automaticamente.
     */
    fun addProductToSale(product: Product, quantity: Int) {
        _uiState.update { state ->
            val newItem = SaleItem(
                productId = product.id,
                productName = product.name,
                quantity = quantity,
                unitPrice = product.price // Supondo que Product tenha 'price'
            )

            // Cria nova lista de itens
            val newItems = state.currentSale.items + newItem

            // Recalcula total
            val newTotal = newItems.sumOf { it.subtotal }

            state.copy(
                currentSale = state.currentSale.copy(
                    items = newItems,
                    totalValue = newTotal
                )
            )
        }
    }

    /**
     * Remove um item da venda.
     */
    fun removeItem(itemIndex: Int) {
        _uiState.update { state ->
            val newItems = state.currentSale.items.toMutableList().apply {
                removeAt(itemIndex)
            }
            val newTotal = newItems.sumOf { it.subtotal }

            state.copy(
                currentSale = state.currentSale.copy(
                    items = newItems,
                    totalValue = newTotal
                )
            )
        }
    }

    /**
     * Atualiza o número de parcelas.
     */
    fun updateInstallments(count: Int) {
        _uiState.update { state ->
            state.copy(currentSale = state.currentSale.copy(installments = count))
        }
    }

    /**
     * Atualiza a observação.
     */
    fun updateObservation(text: String) {
        _uiState.update { state ->
            state.copy(currentSale = state.currentSale.copy(observation = text))
        }
    }

    /**
     * Salva a venda usando o UseCase (que injeta o ID do device automaticamente).
     */
    fun saveSale() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = createSaleUseCase(_uiState.value.currentSale)

            result.onSuccess {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        saveSuccess = true,
                        currentSale = Sale() // Limpa o formulário para nova venda
                    )
                }
            }.onFailure { error ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Erro desconhecido"
                    )
                }
            }
        }
    }

    fun resetSuccessState() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}
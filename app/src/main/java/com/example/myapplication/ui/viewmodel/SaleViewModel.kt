////package com.example.myapplication.ui.viewmodel
////
////import androidx.lifecycle.ViewModel
////import androidx.lifecycle.viewModelScope
////import com.example.myapplication.domain.model.Client
////import com.example.myapplication.domain.model.Product
////import com.example.myapplication.domain.model.Sale
////import com.example.myapplication.domain.model.SaleItem
////import com.example.myapplication.domain.repository.ClientRepository
////import com.example.myapplication.domain.repository.ProductRepository
////import com.example.myapplication.domain.usecase.CreateSaleUseCase
////import dagger.hilt.android.lifecycle.HiltViewModel
////import kotlinx.coroutines.flow.MutableStateFlow
////import kotlinx.coroutines.flow.StateFlow
////import kotlinx.coroutines.flow.asStateFlow
////import kotlinx.coroutines.flow.update
////import kotlinx.coroutines.launch
////import javax.inject.Inject
////
////// ----------------------------------------------------
////// MODELO DE ESTADO DA UI (SALE)
////// ----------------------------------------------------
////data class SaleUiState(
////    val currentSale: Sale = Sale(), // A venda sendo construída
////    val availableProducts: List<Product> = emptyList(), // Lista para o modal de seleção
////    val availableClients: List<Client> = emptyList(), // Lista para o modal de seleção
////    val isLoading: Boolean = false,
////    val saveSuccess: Boolean = false,
////    val errorMessage: String? = null
////)
////
////// ----------------------------------------------------
////// VIEW MODEL
////// ----------------------------------------------------
////@HiltViewModel
////class SaleViewModel @Inject constructor(
////    private val createSaleUseCase: CreateSaleUseCase,
////    private val productRepository: ProductRepository, // Para listar produtos na tela
////    private val clientRepository: ClientRepository    // Para listar clientes na tela
////) : ViewModel() {
////
////    private val _uiState = MutableStateFlow(SaleUiState())
////    val uiState: StateFlow<SaleUiState> = _uiState.asStateFlow()
////
////    init {
////        // Carrega as listas necessárias para fazer uma venda
////        loadDependencies()
////    }
////
////    private fun loadDependencies() {
////        viewModelScope.launch {
////            // Carrega produtos
////            launch {
////                productRepository.getAllProducts().collect { products ->
////                    _uiState.update { it.copy(availableProducts = products) }
////                }
////            }
////            // Carrega clientes
////            launch {
////                clientRepository.getAllClients().collect { clients ->
////                    _uiState.update { it.copy(availableClients = clients) }
////                }
////            }
////        }
////    }
////
////    /**
////     * Define o cliente da venda.
////     */
////    fun selectClient(client: Client) {
////        _uiState.update { state ->
////            state.copy(
////                currentSale = state.currentSale.copy(
////                    clientId = client.id,
////                    clientName = client.name
////                )
////            )
////        }
////    }
////
////    /**
////     * Adiciona um produto à lista de itens da venda.
////     * Recalcula o total automaticamente.
////     */
////    fun addProductToSale(product: Product, quantity: Int) {
////        _uiState.update { state ->
////            val newItem = SaleItem(
////                productId = product.id,
////                productName = product.name,
////                quantity = quantity,
////                unitPrice = product.price // Supondo que Product tenha 'price'
////            )
////
////            // Cria nova lista de itens
////            val newItems = state.currentSale.items + newItem
////
////            // Recalcula total
////            val newTotal = newItems.sumOf { it.subtotal }
////
////            state.copy(
////                currentSale = state.currentSale.copy(
////                    items = newItems,
////                    totalValue = newTotal
////                )
////            )
////        }
////    }
////
////    /**
////     * Remove um item da venda.
////     */
////    fun removeItem(itemIndex: Int) {
////        _uiState.update { state ->
////            val newItems = state.currentSale.items.toMutableList().apply {
////                removeAt(itemIndex)
////            }
////            val newTotal = newItems.sumOf { it.subtotal }
////
////            state.copy(
////                currentSale = state.currentSale.copy(
////                    items = newItems,
////                    totalValue = newTotal
////                )
////            )
////        }
////    }
////
////    /**
////     * Atualiza o número de parcelas.
////     */
////    fun updateInstallments(count: Int) {
////        _uiState.update { state ->
////            state.copy(currentSale = state.currentSale.copy(installments = count))
////        }
////    }
////
////    /**
////     * Atualiza a observação.
////     */
////    fun updateObservation(text: String) {
////        _uiState.update { state ->
////            state.copy(currentSale = state.currentSale.copy(observation = text))
////        }
////    }
////
////    /**
////     * Salva a venda usando o UseCase (que injeta o ID do device automaticamente).
////     */
////    fun saveSale() {
////        viewModelScope.launch {
////            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
////
////            val result = createSaleUseCase(_uiState.value.currentSale)
////
////            result.onSuccess {
////                _uiState.update { state ->
////                    state.copy(
////                        isLoading = false,
////                        saveSuccess = true,
////                        currentSale = Sale() // Limpa o formulário para nova venda
////                    )
////                }
////            }.onFailure { error ->
////                _uiState.update { state ->
////                    state.copy(
////                        isLoading = false,
////                        errorMessage = error.message ?: "Erro desconhecido"
////                    )
////                }
////            }
////        }
////    }
////
////    fun resetSuccessState() {
////        _uiState.update { it.copy(saveSuccess = false) }
////    }
////}
//
//
//package com.example.myapplication.ui.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.myapplication.domain.model.Client
//import com.example.myapplication.domain.model.Product
//import com.example.myapplication.domain.model.Sale
//import com.example.myapplication.domain.model.SaleItem
//import com.example.myapplication.domain.repository.ClientRepository
//import com.example.myapplication.domain.repository.ProductRepository
//import com.example.myapplication.domain.usecase.CreateSaleUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//// ----------------------------------------------------
//// MODELO DE ESTADO DA UI (SALE)
//// ----------------------------------------------------
//data class SaleUiState(
//    val currentSale: Sale = Sale(), // A venda sendo construída
//    val availableProducts: List<Product> = emptyList(), // Lista para o modal de seleção
//    val availableClients: List<Client> = emptyList(), // Lista para o modal de seleção
//    val isLoading: Boolean = false,
//    val saveSuccess: Boolean = false,
//    val errorMessage: String? = null
//)
//
//// ----------------------------------------------------
//// VIEW MODEL
//// ----------------------------------------------------
//@HiltViewModel
//class SaleViewModel @Inject constructor(
//    private val createSaleUseCase: CreateSaleUseCase,
//    private val productRepository: ProductRepository, // Para listar produtos na tela
//    private val clientRepository: ClientRepository    // Para listar clientes na tela
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(SaleUiState())
//    val uiState: StateFlow<SaleUiState> = _uiState.asStateFlow()
//
//    init {
//        // Carrega as listas necessárias para fazer uma venda
//        loadDependencies()
//    }
//
//    private fun loadDependencies() {
//        viewModelScope.launch {
//            // Carrega produtos
//            launch {
//                productRepository.getAllProducts().collect { products ->
//                    _uiState.update { it.copy(availableProducts = products) }
//                }
//            }
//            // Carrega clientes
//            launch {
//                clientRepository.getAllClients().collect { clients ->
//                    _uiState.update { it.copy(availableClients = clients) }
//                }
//            }
//        }
//    }
//
//    /**
//     * Define o cliente da venda.
//     */
//    fun selectClient(client: Client) {
//        _uiState.update { state ->
//            state.copy(
//                currentSale = state.currentSale.copy(
//                    clientId = client.id,
//                    clientName = client.name
//                )
//            )
//        }
//    }
//
//    /**
//     * Remove o cliente selecionado (Deixa em branco).
//     */
//    fun clearClient() {
//        _uiState.update { state ->
//            state.copy(
//                currentSale = state.currentSale.copy(
//                    clientId = "",
//                    clientName = ""
//                )
//            )
//        }
//    }
//
//    /**
//     * Adiciona um produto à lista de itens da venda.
//     * Recalcula o total automaticamente.
//     */
//    fun addProductToSale(product: Product, quantity: Int) {
//        _uiState.update { state ->
//            val newItem = SaleItem(
//                productId = product.id,
//                productName = product.name,
//                quantity = quantity,
//                unitPrice = product.price
//            )
//
//            // Cria nova lista de itens
//            val newItems = state.currentSale.items + newItem
//
//            // Recalcula total
//            val newTotal = newItems.sumOf { it.subtotal }
//
//            state.copy(
//                currentSale = state.currentSale.copy(
//                    items = newItems,
//                    totalValue = newTotal
//                )
//            )
//        }
//    }
//
//    /**
//     * Remove um item da venda.
//     */
//    fun removeItem(itemIndex: Int) {
//        _uiState.update { state ->
//            val newItems = state.currentSale.items.toMutableList().apply {
//                removeAt(itemIndex)
//            }
//            val newTotal = newItems.sumOf { it.subtotal }
//
//            state.copy(
//                currentSale = state.currentSale.copy(
//                    items = newItems,
//                    totalValue = newTotal
//                )
//            )
//        }
//    }
//
//    /**
//     * Atualiza o número de parcelas.
//     */
//    fun updateInstallments(count: Int) {
//        _uiState.update { state ->
//            state.copy(currentSale = state.currentSale.copy(installments = count))
//        }
//    }
//
//    /**
//     * Atualiza a observação.
//     */
//    fun updateObservation(text: String) {
//        _uiState.update { state ->
//            state.copy(currentSale = state.currentSale.copy(observation = text))
//        }
//    }
//
//    /**
//     * Salva a venda usando o UseCase.
//     */
//    fun saveSale() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
//
//            val result = createSaleUseCase(_uiState.value.currentSale)
//
//            result.onSuccess {
//                _uiState.update { state ->
//                    state.copy(
//                        isLoading = false,
//                        saveSuccess = true,
//                        currentSale = Sale() // Limpa o formulário para nova venda
//                    )
//                }
//            }.onFailure { error ->
//                _uiState.update { state ->
//                    state.copy(
//                        isLoading = false,
//                        errorMessage = error.message ?: "Erro desconhecido"
//                    )
//                }
//            }
//        }
//    }
//
//    fun resetSuccessState() {
//        _uiState.update { it.copy(saveSuccess = false) }
//    }
//}


package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Client
import com.example.myapplication.domain.model.Installment
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
import java.util.Calendar
import javax.inject.Inject

// ----------------------------------------------------
// MODELO DE ESTADO DA UI (SALE)
// ----------------------------------------------------
data class SaleUiState(
    val currentSale: Sale = Sale(),
    val installmentCount: Int = 1, // Guardamos o número separado para refazer contas
    val availableProducts: List<Product> = emptyList(),
    val availableClients: List<Client> = emptyList(),
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
    private val productRepository: ProductRepository,
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SaleUiState())
    val uiState: StateFlow<SaleUiState> = _uiState.asStateFlow()

    init {
        loadDependencies()
    }

    private fun loadDependencies() {
        viewModelScope.launch {
            launch {
                productRepository.getAllProducts().collect { products ->
                    _uiState.update { it.copy(availableProducts = products) }
                }
            }
            launch {
                clientRepository.getAllClients().collect { clients ->
                    _uiState.update { it.copy(availableClients = clients) }
                }
            }
        }
    }

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

    fun clearClient() {
        _uiState.update { state ->
            state.copy(
                currentSale = state.currentSale.copy(
                    clientId = "",
                    clientName = ""
                )
            )
        }
    }

    fun addProductToSale(product: Product, quantity: Int) {
        _uiState.update { state ->
            val newItem = SaleItem(
                productId = product.id,
                productName = product.name,
                quantity = quantity,
                unitPrice = product.price
            )
            val newItems = state.currentSale.items + newItem
            val newTotal = newItems.sumOf { it.subtotal }

            // IMPORTANTE: Recalcular as parcelas com o novo total
            val newInstallments = calculateInstallments(newTotal, state.installmentCount)

            state.copy(
                currentSale = state.currentSale.copy(
                    items = newItems,
                    totalValue = newTotal,
                    installments = newInstallments // Atualiza a lista de parcelas
                )
            )
        }
    }

    fun removeItem(itemIndex: Int) {
        _uiState.update { state ->
            val newItems = state.currentSale.items.toMutableList().apply {
                removeAt(itemIndex)
            }
            val newTotal = newItems.sumOf { it.subtotal }

            // IMPORTANTE: Recalcular as parcelas com o novo total
            val newInstallments = calculateInstallments(newTotal, state.installmentCount)

            state.copy(
                currentSale = state.currentSale.copy(
                    items = newItems,
                    totalValue = newTotal,
                    installments = newInstallments
                )
            )
        }
    }

    /**
     * Atualiza a QUANTIDADE de parcelas e regenera a lista de objetos.
     */
    fun updateInstallmentsCount(count: Int) {
        _uiState.update { state ->
            val safeCount = if (count < 1) 1 else count

            // Gera a lista baseada no total atual e na nova quantidade
            val newInstallments = calculateInstallments(state.currentSale.totalValue, safeCount)

            state.copy(
                installmentCount = safeCount,
                currentSale = state.currentSale.copy(installments = newInstallments)
            )
        }
    }

    fun updateObservation(text: String) {
        _uiState.update { state ->
            state.copy(currentSale = state.currentSale.copy(observation = text))
        }
    }

    fun saveSale() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = createSaleUseCase(_uiState.value.currentSale)

            result.onSuccess {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        saveSuccess = true,
                        currentSale = Sale(), // Reseta Venda
                        installmentCount = 1  // Reseta contador
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

    // ----------------------------------------------------
    // LÓGICA PRIVADA DE CÁLCULO FINANCEIRO
    // ----------------------------------------------------
    private fun calculateInstallments(totalValue: Double, count: Int): List<Installment> {
        if (totalValue <= 0) return emptyList()

        val installments = mutableListOf<Installment>()
        val valuePerInstallment = totalValue / count

        // Pega data atual
        val calendar = Calendar.getInstance()

        for (i in 1..count) {
            // Adiciona 1 mês (30 dias aprox) para cada parcela
            calendar.add(Calendar.MONTH, 1)

            installments.add(
                Installment(
                    id = "",       // ID será gerado no UseCase
                    saleId = "",   // ID será gerado no UseCase
                    clientId = "", // <--- CORREÇÃO: Inicializa vazio aqui, UseCase preenche
                    number = i,
                    dueDate = calendar.timeInMillis,
                    originalValue = valuePerInstallment,
                    amountPaid = 0.0
                )
            )
        }
        return installments
    }
}
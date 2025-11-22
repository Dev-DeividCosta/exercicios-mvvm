//package com.example.myapplication.ui.viewmodel
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.myapplication.domain.model.Client
//import com.example.myapplication.domain.model.Installment
//import com.example.myapplication.domain.repository.ClientRepository
//import com.example.myapplication.domain.repository.InstallmentRepository
//import com.example.myapplication.domain.usecase.ReceivePaymentUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.onStart
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//// ----------------------------------------------------
//// MODELO DE ESTADO DA TELA DE ROTA
//// ----------------------------------------------------
//data class RouteUiState(
//    val selectedCity: String = "",
//    val availableCities: List<String> = emptyList(),
//    val clientsInRoute: List<Client> = emptyList(),
//    val clientDebts: Map<String, List<Installment>> = emptyMap(),
//    val expandedClientId: String? = null,
//    val isReorderMode: Boolean = false,
//    val isLoading: Boolean = false,
//    val errorMessage: String? = null,
//    val paymentSuccess: Boolean = false
//)
//
//// ----------------------------------------------------
//// VIEW MODEL DA ROTA
//// ----------------------------------------------------
//@HiltViewModel
//class RouteViewModel @Inject constructor(
//    private val clientRepository: ClientRepository,
//    private val installmentRepository: InstallmentRepository,
//    private val receivePaymentUseCase: ReceivePaymentUseCase
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(RouteUiState())
//    val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()
//
//    private var allClientsCache: List<Client> = emptyList()
//
//    init {
//        loadInitialData()
//    }
//
//    private fun loadInitialData() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true) }
//            Log.d("RouteViewModel", "Iniciando carregamento de clientes...")
//
//            clientRepository.getAllClients()
//                .onStart { Log.d("RouteViewModel", "Flow iniciado, aguardando dados...") }
//                .catch { e ->
//                    Log.e("RouteViewModel", "Erro ao carregar clientes: ${e.message}")
//                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
//                }
//                .collect { clients ->
//                    Log.d("RouteViewModel", "Clientes recebidos do Firebase: ${clients.size}")
//
//                    allClientsCache = clients
//
//                    // Debug: Ver o que está chegando
//                    clients.take(3).forEach {
//                        Log.d("RouteViewModel", "Cliente: ${it.name} - Cidade crua: '${it.address.city}'")
//                    }
//
//                    // 1. Extrai cidades únicas limpando os espaços (.trim())
//                    val cities = clients
//                        .map { it.address.city.trim() }
//                        .filter { it.isNotBlank() }
//                        .distinct()
//                        .sorted()
//
//                    Log.d("RouteViewModel", "Cidades disponíveis para filtro: $cities")
//
//                    // 2. Reaplica o filtro se já tiver cidade selecionada
//                    val currentCity = _uiState.value.selectedCity
//                    val filteredClients = if (currentCity.isNotBlank()) {
//                        allClientsCache
//                            .filter {
//                                // CORREÇÃO CRÍTICA 1: .trim() aqui para ignorar espaços no banco
//                                it.address.city.trim().equals(currentCity, ignoreCase = true)
//                            }
//                            .sortedBy { it.routeIndex }
//                    } else {
//                        emptyList()
//                    }
//
//                    _uiState.update { state ->
//                        state.copy(
//                            isLoading = false,
//                            availableCities = cities,
//                            clientsInRoute = filteredClients
//                        )
//                    }
//
//                    if (filteredClients.isNotEmpty()) {
//                        loadDebtsForClients(filteredClients)
//                    }
//                }
//        }
//    }
//
//    fun selectCity(city: String) {
//        _uiState.update { it.copy(selectedCity = city) }
//        filterClientsByCity(city)
//    }
//
//    private fun filterClientsByCity(city: String) {
//        Log.d("RouteViewModel", "Filtrando por cidade: '$city'")
//
//        val filtered = allClientsCache
//            .filter {
//                // CORREÇÃO CRÍTICA 2: .trim() aqui também
//                it.address.city.trim().equals(city, ignoreCase = true)
//            }
//            .sortedBy { it.routeIndex }
//
//        Log.d("RouteViewModel", "Clientes encontrados nessa cidade: ${filtered.size}")
//
//        _uiState.update { it.copy(clientsInRoute = filtered) }
//        loadDebtsForClients(filtered)
//    }
//
//    private fun loadDebtsForClients(clients: List<Client>) {
//        viewModelScope.launch {
//            val debtMap = _uiState.value.clientDebts.toMutableMap()
//            clients.forEach { client ->
//                try {
//                    // Busca parcelas usando o ID do cliente (que agora existe no installment)
//                    val installments = installmentRepository.getInstallmentsByClientId(client.id).first()
//                    debtMap[client.id] = installments
//                } catch (e: Exception) {
//                    Log.e("RouteViewModel", "Erro ao carregar dívida de ${client.name}: ${e.message}")
//                }
//            }
//            _uiState.update { it.copy(clientDebts = debtMap) }
//        }
//    }
//
//    fun toggleClientExpanded(clientId: String) {
//        _uiState.update { state ->
//            val newExpandedId = if (state.expandedClientId == clientId) null else clientId
//            state.copy(expandedClientId = newExpandedId)
//        }
//    }
//
//    fun registerPayment(installment: Installment, amountReceived: Double) {
//        viewModelScope.launch {
//            val result = receivePaymentUseCase(installment, amountReceived)
//            result.onSuccess {
//                _uiState.update { it.copy(paymentSuccess = true) }
//            }.onFailure { error ->
//                _uiState.update { it.copy(errorMessage = error.message) }
//            }
//        }
//    }
//
//    fun updateReferencePoint(clientId: String, newReference: String) {
//        viewModelScope.launch {
//            val currentUserId = "user_padrao"
//            clientRepository.updateClientComplement(clientId, currentUserId, newReference)
//        }
//    }
//
//    fun toggleReorderMode() {
//        _uiState.update { it.copy(isReorderMode = !it.isReorderMode) }
//    }
//
//    fun saveNewRouteOrder(reorderedClients: List<Client>) {
//        viewModelScope.launch {
//            _uiState.update { it.copy(clientsInRoute = reorderedClients) }
//
//            reorderedClients.forEachIndexed { index, client ->
//                if (client.routeIndex != index) {
//                    val updatedClient = client.copy(routeIndex = index)
//                    clientRepository.updateClient(updatedClient)
//                }
//            }
//        }
//    }
//
//    fun clearError() {
//        _uiState.update { it.copy(errorMessage = null) }
//    }
//
//    fun resetPaymentSuccess() {
//        _uiState.update { it.copy(paymentSuccess = false) }
//    }
//}


//package com.example.myapplication.ui.viewmodel
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.myapplication.domain.model.Client
//import com.example.myapplication.domain.model.Installment
//import com.example.myapplication.domain.repository.ClientRepository
//import com.example.myapplication.domain.repository.InstallmentRepository
//import com.example.myapplication.domain.usecase.ReceivePaymentUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.onStart
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import java.util.Collections
//import javax.inject.Inject
//
//// ----------------------------------------------------
//// MODELO DE ESTADO
//// ----------------------------------------------------
//data class RouteUiState(
//    val selectedCity: String = "",
//    val availableCities: List<String> = emptyList(),
//    val clientsInRoute: List<Client> = emptyList(),
//    val clientDebts: Map<String, List<Installment>> = emptyMap(),
//    val expandedClientId: String? = null,
//    val isReorderMode: Boolean = false,
//    val isLoading: Boolean = false,
//    val errorMessage: String? = null,
//    val paymentSuccess: Boolean = false
//)
//
//// ----------------------------------------------------
//// VIEW MODEL
//// ----------------------------------------------------
//@HiltViewModel
//class RouteViewModel @Inject constructor(
//    private val clientRepository: ClientRepository,
//    private val installmentRepository: InstallmentRepository,
//    private val receivePaymentUseCase: ReceivePaymentUseCase
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(RouteUiState())
//    val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()
//
//    private var allClientsCache: List<Client> = emptyList()
//
//    init {
//        loadInitialData()
//    }
//
//    private fun loadInitialData() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true) }
//
//            clientRepository.getAllClients()
//                .onStart { Log.d("RouteViewModel", "Aguardando dados...") }
//                .catch { e ->
//                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
//                }
//                .collect { clients ->
//                    allClientsCache = clients
//
//                    val cities = clients
//                        .map { it.address.city.trim() }
//                        .filter { it.isNotBlank() }
//                        .distinct()
//                        .sorted()
//
//                    val currentCity = _uiState.value.selectedCity
//                    val filteredClients = if (currentCity.isNotBlank()) {
//                        allClientsCache
//                            .filter { it.address.city.trim().equals(currentCity, ignoreCase = true) }
//                            .sortedBy { it.routeIndex }
//                    } else {
//                        emptyList()
//                    }
//
//                    _uiState.update { state ->
//                        state.copy(
//                            isLoading = false,
//                            availableCities = cities,
//                            clientsInRoute = filteredClients
//                        )
//                    }
//
//                    if (filteredClients.isNotEmpty()) {
//                        loadDebtsForClients(filteredClients)
//                    }
//                }
//        }
//    }
//
//    fun selectCity(city: String) {
//        _uiState.update { it.copy(selectedCity = city) }
//        filterClientsByCity(city)
//    }
//
//    private fun filterClientsByCity(city: String) {
//        val filtered = allClientsCache
//            .filter { it.address.city.trim().equals(city, ignoreCase = true) }
//            .sortedBy { it.routeIndex }
//
//        _uiState.update { it.copy(clientsInRoute = filtered) }
//        loadDebtsForClients(filtered)
//    }
//
//    private fun loadDebtsForClients(clients: List<Client>) {
//        viewModelScope.launch {
//            val debtMap = _uiState.value.clientDebts.toMutableMap()
//            clients.forEach { client ->
//                try {
//                    val installments = installmentRepository.getInstallmentsByClientId(client.id).first()
//                    debtMap[client.id] = installments
//                } catch (e: Exception) {
//                    Log.e("RouteViewModel", "Erro dívida: ${e.message}")
//                }
//            }
//            _uiState.update { it.copy(clientDebts = debtMap) }
//        }
//    }
//
//    fun toggleClientExpanded(clientId: String) {
//        _uiState.update { state ->
//            val newExpandedId = if (state.expandedClientId == clientId) null else clientId
//            state.copy(expandedClientId = newExpandedId)
//        }
//    }
//
//    fun registerPayment(installment: Installment, amountReceived: Double) {
//        viewModelScope.launch {
//            val result = receivePaymentUseCase(installment, amountReceived)
//            result.onSuccess {
//                _uiState.update { it.copy(paymentSuccess = true) }
//            }.onFailure { error ->
//                _uiState.update { it.copy(errorMessage = error.message) }
//            }
//        }
//    }
//
//    fun updateReferencePoint(clientId: String, newReference: String) {
//        viewModelScope.launch {
//            val currentUserId = "user_padrao"
//            clientRepository.updateClientComplement(clientId, currentUserId, newReference)
//        }
//    }
//
//    fun toggleReorderMode() {
//        _uiState.update { it.copy(isReorderMode = !it.isReorderMode) }
//    }
//
//    /**
//     * NOVO: Move um cliente na lista e salva a nova ordem
//     */
//    fun moveClient(fromIndex: Int, toIndex: Int) {
//        val currentList = _uiState.value.clientsInRoute.toMutableList()
//
//        if (fromIndex in currentList.indices && toIndex in currentList.indices) {
//            // 1. Troca na lista local (Atualização visual imediata)
//            Collections.swap(currentList, fromIndex, toIndex)
//            _uiState.update { it.copy(clientsInRoute = currentList) }
//
//            // 2. Salva a nova ordem no Firebase
//            saveNewRouteOrder(currentList)
//        }
//    }
//
//    private fun saveNewRouteOrder(reorderedClients: List<Client>) {
//        viewModelScope.launch {
//            reorderedClients.forEachIndexed { index, client ->
//                // Só atualiza no banco se o índice mudou
//                if (client.routeIndex != index) {
//                    val updatedClient = client.copy(routeIndex = index)
//                    clientRepository.updateClient(updatedClient)
//                }
//            }
//        }
//    }
//
//    fun clearError() {
//        _uiState.update { it.copy(errorMessage = null) }
//    }
//
//    fun resetPaymentSuccess() {
//        _uiState.update { it.copy(paymentSuccess = false) }
//    }
//}
//

package com.example.myapplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Client
import com.example.myapplication.domain.model.Installment
import com.example.myapplication.domain.model.Sale
import com.example.myapplication.domain.model.SaleItem
import com.example.myapplication.domain.repository.ClientRepository
import com.example.myapplication.domain.repository.InstallmentRepository
import com.example.myapplication.domain.usecase.ReceivePaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

// ----------------------------------------------------
// MODELO DE ESTADO
// ----------------------------------------------------
data class RouteUiState(
    val selectedCity: String = "",
    val availableCities: List<String> = emptyList(),
    val clientsInRoute: List<Client> = emptyList(),
    val clientDebts: Map<String, List<Installment>> = emptyMap(),
    val expandedClientId: String? = null,

    // --- NOVOS CAMPOS PARA O MODAL DE DETALHES ---
    val selectedSale: Sale? = null, // A venda atualmente aberta no modal
    val isSaleLoading: Boolean = false,

    val isReorderMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val paymentSuccess: Boolean = false
)

// ----------------------------------------------------
// VIEW MODEL
// ----------------------------------------------------
@HiltViewModel
class RouteViewModel @Inject constructor(
    private val clientRepository: ClientRepository,
    private val installmentRepository: InstallmentRepository,
    private val receivePaymentUseCase: ReceivePaymentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RouteUiState())
    val uiState: StateFlow<RouteUiState> = _uiState.asStateFlow()

    private var allClientsCache: List<Client> = emptyList()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            clientRepository.getAllClients()
                .onStart { Log.d("RouteViewModel", "Aguardando dados...") }
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
                .collect { clients ->
                    allClientsCache = clients

                    val cities = clients
                        .map { it.address.city.trim() }
                        .filter { it.isNotBlank() }
                        .distinct()
                        .sorted()

                    val currentCity = _uiState.value.selectedCity
                    val filteredClients = if (currentCity.isNotBlank()) {
                        allClientsCache
                            .filter { it.address.city.trim().equals(currentCity, ignoreCase = true) }
                            .sortedBy { it.routeIndex }
                    } else {
                        emptyList()
                    }

                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            availableCities = cities,
                            clientsInRoute = filteredClients
                        )
                    }

                    if (filteredClients.isNotEmpty()) {
                        loadDebtsForClients(filteredClients)
                    }
                }
        }
    }

    fun selectCity(city: String) {
        _uiState.update { it.copy(selectedCity = city) }
        filterClientsByCity(city)
    }

    private fun filterClientsByCity(city: String) {
        val filtered = allClientsCache
            .filter { it.address.city.trim().equals(city, ignoreCase = true) }
            .sortedBy { it.routeIndex }

        _uiState.update { it.copy(clientsInRoute = filtered) }
        loadDebtsForClients(filtered)
    }

    private fun loadDebtsForClients(clients: List<Client>) {
        viewModelScope.launch {
            val debtMap = _uiState.value.clientDebts.toMutableMap()
            clients.forEach { client ->
                try {
                    val installments = installmentRepository.getInstallmentsByClientId(client.id).first()
                    debtMap[client.id] = installments
                } catch (e: Exception) {
                    Log.e("RouteViewModel", "Erro dívida: ${e.message}")
                }
            }
            _uiState.update { it.copy(clientDebts = debtMap) }

            // Se tiver uma venda aberta, atualiza ela também (caso tenha mudado algo)
            val currentSaleId = _uiState.value.selectedSale?.id
            if (currentSaleId != null) {
                refreshOpenSale(currentSaleId, debtMap)
            }
        }
    }

    // ----------------------------------------------------
    // LÓGICA DO MODAL DE DETALHES (NOVO)
    // ----------------------------------------------------

    fun openSaleDetails(saleId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaleLoading = true) }

            // 1. Recupera as parcelas dessa venda que já temos em memória
            val allInstallments = _uiState.value.clientDebts.values.flatten()
            val saleInstallments = allInstallments.filter { it.saleId == saleId }

            if (saleInstallments.isNotEmpty()) {
                // Tenta achar o cliente dono da venda
                val clientId = saleInstallments.first().clientId
                val client = allClientsCache.find { it.id == clientId }

                val totalValue = saleInstallments.sumOf { it.originalValue }

                // 2. RECONSTRUÇÃO DA VENDA (MOCK INTELIGENTE)
                // Como ainda não temos o `getSaleById` com os itens reais no repositório,
                // montamos o objeto Sale aqui para a UI exibir.
                // TODO: Futuramente, chamar `saleRepository.getSaleById(saleId)` aqui.

                val simulatedSale = Sale(
                    id = saleId,
                    clientId = clientId,
                    clientName = client?.name ?: "Cliente Desconhecido",
                    // ITENS FICTÍCIOS PARA VISUALIZAÇÃO (Substituir por dados reais do DB depois)
                    items = listOf(
                        SaleItem("1", "Produto Exemplo A", 1, totalValue * 0.4),
                        SaleItem("2", "Produto Exemplo B", 2, totalValue * 0.3)
                    ),
                    installments = saleInstallments,
                    totalValue = totalValue,
                    observation = "Venda registrada via app.",
                    date = saleInstallments.minOfOrNull { it.dueDate } ?: System.currentTimeMillis(), // Usando data da 1a parcela como ref
                    sellerDeviceId = "user_padrao"
                )

                _uiState.update { it.copy(selectedSale = simulatedSale, isSaleLoading = false) }
            } else {
                _uiState.update { it.copy(isSaleLoading = false, errorMessage = "Venda não encontrada.") }
            }
        }
    }

    fun closeSaleDetails() {
        _uiState.update { it.copy(selectedSale = null) }
    }

    // Atualiza a venda aberta (útil após um pagamento) sem fechar o modal
    private fun refreshOpenSale(saleId: String, currentDebtMap: Map<String, List<Installment>>) {
        val allInstallments = currentDebtMap.values.flatten()
        val updatedInstallments = allInstallments.filter { it.saleId == saleId }

        if (updatedInstallments.isNotEmpty()) {
            _uiState.update { state ->
                val currentSale = state.selectedSale
                if (currentSale != null && currentSale.id == saleId) {
                    state.copy(selectedSale = currentSale.copy(installments = updatedInstallments))
                } else {
                    state
                }
            }
        }
    }

    // ----------------------------------------------------
    // AÇÕES GERAIS
    // ----------------------------------------------------

    fun toggleClientExpanded(clientId: String) {
        _uiState.update { state ->
            val newExpandedId = if (state.expandedClientId == clientId) null else clientId
            state.copy(expandedClientId = newExpandedId)
        }
    }

    fun registerPayment(installment: Installment, amountReceived: Double) {
        viewModelScope.launch {
            val result = receivePaymentUseCase(installment, amountReceived)
            result.onSuccess {
                _uiState.update { it.copy(paymentSuccess = true) }

                // Recarrega os dados do cliente específico para atualizar a UI e o Modal
                val currentClient = allClientsCache.find { it.id == installment.clientId }
                if (currentClient != null) {
                    // Pequeno delay para garantir que o banco atualizou (opcional, dependo do DB)
                    loadDebtsForClients(listOf(currentClient))
                }

            }.onFailure { error ->
                _uiState.update { it.copy(errorMessage = error.message) }
            }
        }
    }

    fun updateReferencePoint(clientId: String, newReference: String) {
        viewModelScope.launch {
            val currentUserId = "user_padrao"
            clientRepository.updateClientComplement(clientId, currentUserId, newReference)
            // Atualiza o cache local para refletir na UI imediatamente se necessário
            // (Dependendo de como seu repository funciona, talvez precise recarregar os clientes)
        }
    }

    fun toggleReorderMode() {
        _uiState.update { it.copy(isReorderMode = !it.isReorderMode) }
    }

    fun moveClient(fromIndex: Int, toIndex: Int) {
        val currentList = _uiState.value.clientsInRoute.toMutableList()

        if (fromIndex in currentList.indices && toIndex in currentList.indices) {
            Collections.swap(currentList, fromIndex, toIndex)
            _uiState.update { it.copy(clientsInRoute = currentList) }
            saveNewRouteOrder(currentList)
        }
    }

    private fun saveNewRouteOrder(reorderedClients: List<Client>) {
        viewModelScope.launch {
            reorderedClients.forEachIndexed { index, client ->
                if (client.routeIndex != index) {
                    val updatedClient = client.copy(routeIndex = index)
                    clientRepository.updateClient(updatedClient)
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun resetPaymentSuccess() {
        _uiState.update { it.copy(paymentSuccess = false) }
    }
}
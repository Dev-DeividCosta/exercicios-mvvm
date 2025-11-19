package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Client
import com.example.myapplication.domain.model.Address
import com.example.myapplication.domain.usecase.CreateClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ----------------------------------------------------
// MODELO DE ESTADO DA UI
// ----------------------------------------------------
data class ClientUiState(
    val client: Client = Client(),
    val isLoading: Boolean = false,
    val isFormValid: Boolean = true,
    val errorMessage: String? = null,
    val saveSuccess: Boolean = false
)

// ----------------------------------------------------
// VIEW MODEL
// ----------------------------------------------------
@HiltViewModel
class ClientViewModel @Inject constructor(
    private val createClientUseCase: CreateClientUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientUiState(isFormValid = true))
    val uiState: StateFlow<ClientUiState> = _uiState

    /**
     * Atualiza um campo de texto.
     */
    fun onFieldChange(field: String, value: String) {
        _uiState.update { currentState ->
            val updatedClient = when (field) {
                "name" -> currentState.client.copy(name = value)
                "cpf" -> currentState.client.copy(cpf = value)
                "phone" -> currentState.client.copy(phone = value)
                // Acesso aninhado para o Address
                "zipCode" -> currentState.client.copy(address = currentState.client.address.copy(zipCode = value))
                "city" -> currentState.client.copy(address = currentState.client.address.copy(city = value))
                "street" -> currentState.client.copy(address = currentState.client.address.copy(street = value))
                "houseNumber" -> currentState.client.copy(address = currentState.client.address.copy(houseNumber = value))
                else -> currentState.client
            }

            // Atualiza o estado
            currentState.copy(
                client = updatedClient,
                isFormValid = true,
                errorMessage = null
            )
        }
    }

    /**
     * Orquestra a criação do cliente.
     * Retorna sucesso IMEDIATAMENTE devido à alteração no Repository.
     */
    fun saveClient() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val clientToSave = _uiState.value.client

            try {
                // Chama o Use Case (que agora é instantâneo)
                createClientUseCase(clientToSave)

                // Sucesso (Executado imediatamente)
                _uiState.update { it.copy(
                    isLoading = false,
                    saveSuccess = true,
                    client = Client(),
                    isFormValid = true
                ) }

            } catch (e: Exception) {
                // Erro (Apenas se houver falha de escrita local)
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = "Falha ao salvar: ${e.message}"
                ) }
            }
        }
    }

    /**
     * Limpa o estado de sucesso.
     */
    fun resetSuccessState() {
        _uiState.update { it.copy(saveSuccess = false) }
    }

    // A função validateForm foi mantida, embora atualmente retorne sempre true,
    // garantindo que o botão esteja sempre ativo.
    private fun validateForm(client: Client): Boolean {
        return true
    }
}
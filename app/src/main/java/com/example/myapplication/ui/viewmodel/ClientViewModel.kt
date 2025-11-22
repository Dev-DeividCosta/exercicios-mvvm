package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Client
import com.example.myapplication.domain.usecase.CreateClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClientUiState(
    val client: Client = Client(),
    val complementInput: String = "",
    val isLoading: Boolean = false,
    val isFormValid: Boolean = true,
    val errorMessage: String? = null,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val createClientUseCase: CreateClientUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientUiState())
    val uiState: StateFlow<ClientUiState> = _uiState

    fun onFieldChange(field: String, value: String) {
        _uiState.update { currentState ->
            val (updatedClient, updatedComplement) = when (field) {
                "name" -> currentState.client.copy(name = value) to currentState.complementInput
                "cpf" -> currentState.client.copy(cpf = value) to currentState.complementInput
                "phone" -> currentState.client.copy(phone = value) to currentState.complementInput
                "city" -> currentState.client.copy(
                    address = currentState.client.address.copy(city = value)
                ) to currentState.complementInput
                "neighborhood" -> currentState.client.copy(
                    address = currentState.client.address.copy(neighborhood = value)
                ) to currentState.complementInput
                "street" -> currentState.client.copy(
                    address = currentState.client.address.copy(street = value)
                ) to currentState.complementInput
                "number" -> currentState.client.copy(
                    address = currentState.client.address.copy(number = value)
                ) to currentState.complementInput
                "complement" -> currentState.client to value
                else -> currentState.client to currentState.complementInput
            }

            currentState.copy(
                client = updatedClient,
                complementInput = updatedComplement,
                isFormValid = true,
                errorMessage = null
            )
        }
    }

    fun saveClient(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val currentState = _uiState.value

            try {
                val addressWithComplement = currentState.client.address.copy(
                    userComplements = mapOf(userId to currentState.complementInput)
                )

                val clientToSave = currentState.client.copy(
                    address = addressWithComplement
                )

                createClientUseCase(clientToSave)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        saveSuccess = true,
                        client = Client(),
                        complementInput = "",
                        isFormValid = true
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Falha ao salvar: ${e.message}"
                    )
                }
            }
        }
    }

    fun resetSuccessState() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}

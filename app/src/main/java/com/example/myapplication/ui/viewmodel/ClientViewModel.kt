package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Address
import com.example.myapplication.domain.model.Client
import com.example.myapplication.domain.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val repository: ClientRepository
) : ViewModel() {

    private val _clients = MutableStateFlow<List<Client>>(emptyList())
    val clients: StateFlow<List<Client>> = _clients.asStateFlow()

    init {
        fetchClients()
    }

    private fun fetchClients() {
        viewModelScope.launch {
            repository.getAllClients().collect { lista ->
                _clients.value = lista
            }
        }
    }

    fun saveClient(
        name: String, cpf: String, phone: String,
        city: String, neighborhood: String, street: String, number: String, complement: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val address = Address(
                city = city,
                neighborhood = neighborhood,
                street = street,
                houseNumber = number,
                complement = complement
            )
            val client = Client(
                id = "",
                name = name,
                cpf = cpf,
                phone = phone,
                address = address
            )
            repository.createClient(client)
            onSuccess()
        }
    }
}
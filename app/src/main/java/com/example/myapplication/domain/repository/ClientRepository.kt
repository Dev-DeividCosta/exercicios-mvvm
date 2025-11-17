package com.example.padrecicero.domain.repository

import com.example.myapplication.domain.model.Client
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    suspend fun createClient(client: Client)
    fun getAllClients(): Flow<List<Client>>
    fun getClientById(clientId: String): Flow<Client?>
    suspend fun updateClient(client: Client)
    suspend fun deleteClient(clientId: String)
}
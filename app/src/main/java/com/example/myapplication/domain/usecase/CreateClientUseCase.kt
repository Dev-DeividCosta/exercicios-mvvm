package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.Client
import com.example.myapplication.domain.repository.ClientRepository
import javax.inject.Inject

class CreateClientUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    /**
     * Função de invocação que apenas repassa a chamada de criação
     * diretamente para o ClientRepository, sem regras de negócio ou validação.
     */
    suspend operator fun invoke(client: Client) {
        // SEM REGRAS: Apenas delega a persistência.
        clientRepository.createClient(client)
    }
}
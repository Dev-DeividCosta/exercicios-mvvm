package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.repository.InstallmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetClientDebtOverviewUseCase @Inject constructor(
    private val repository: InstallmentRepository
) {
    // Retorna o valor TOTAL que esse cliente deve (soma de todos os resíduos)
    operator fun invoke(clientId: String): Flow<Double> {
        return repository.getInstallmentsByClientId(clientId)
            .map { installments ->
                // Filtra apenas as que não estão pagas e soma o que falta
                installments
                    .filter { !it.isPaid }
                    .sumOf { it.remainingAmount }
            }
    }
}
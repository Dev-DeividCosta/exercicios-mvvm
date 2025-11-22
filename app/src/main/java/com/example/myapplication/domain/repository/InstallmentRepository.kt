package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Installment
import kotlinx.coroutines.flow.Flow

interface InstallmentRepository {
    suspend fun createInstallments(installments: List<Installment>)
    fun getInstallmentsBySaleId(saleId: String): Flow<List<Installment>>
    fun getInstallmentsByClientId(clientId: String): Flow<List<Installment>>
    suspend fun updateInstallment(installment: Installment)
    suspend fun deleteInstallmentsBySaleId(saleId: String)
}
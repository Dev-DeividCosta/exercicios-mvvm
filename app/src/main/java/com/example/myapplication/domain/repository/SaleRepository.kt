package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Sale
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    suspend fun createSale(sale: Sale)
    fun getAllSales(): Flow<List<Sale>>
    fun getSaleById(saleId: String): Flow<Sale?>
    fun getSalesByClientId(clientId: String): Flow<List<Sale>>
    fun getSalesBySellerId(sellerId: String): Flow<List<Sale>>
    suspend fun updateSale(sale: Sale)
    suspend fun deleteSale(saleId: String)
}
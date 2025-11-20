package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.Sale
import com.example.myapplication.domain.provider.DeviceIdProvider
import com.example.myapplication.domain.repository.SaleRepository
import javax.inject.Inject

class CreateSaleUseCase @Inject constructor(
    private val repository: SaleRepository,
    private val deviceIdProvider: DeviceIdProvider
) {

    suspend operator fun invoke(sale: Sale): Result<Unit> {
        return try {
            if (sale.items.isEmpty()) {
                return Result.failure(Exception("A venda precisa ter pelo menos um item."))
            }
            if (sale.clientId.isBlank()) {
                return Result.failure(Exception("Selecione um cliente."))
            }

            val saleWithDeviceId = sale.copy(
                sellerDeviceId = deviceIdProvider.getDeviceId()
            )

            repository.createSale(saleWithDeviceId)

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
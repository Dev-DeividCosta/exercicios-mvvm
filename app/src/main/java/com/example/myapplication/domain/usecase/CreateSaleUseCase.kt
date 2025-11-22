package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.Sale
import com.example.myapplication.domain.provider.DeviceIdProvider
import com.example.myapplication.domain.repository.InstallmentRepository
import com.example.myapplication.domain.repository.SaleRepository
import java.util.UUID
import javax.inject.Inject

class CreateSaleUseCase @Inject constructor(
    private val saleRepository: SaleRepository,
    private val installmentRepository: InstallmentRepository,
    private val deviceIdProvider: DeviceIdProvider
) {

    suspend operator fun invoke(sale: Sale): Result<Unit> {
        return try {
            // 1. Validações
            if (sale.items.isEmpty()) {
                return Result.failure(Exception("A venda precisa ter pelo menos um item."))
            }
            if (sale.clientId.isBlank()) {
                return Result.failure(Exception("Selecione um cliente."))
            }
            if (sale.installments.isEmpty()) {
                return Result.failure(Exception("Defina as parcelas da venda."))
            }

            // 2. Geração de IDs
            val generatedSaleId = UUID.randomUUID().toString()
            val deviceId = deviceIdProvider.getDeviceId()
            val creationTime = System.currentTimeMillis()

            // 3. Preparar as Parcelas (Installments)
            // AQUI ESTÁ A CORREÇÃO: Preenchemos o clientId na parcela
            val finalInstallments = sale.installments.map { installment ->
                installment.copy(
                    id = UUID.randomUUID().toString(),
                    saleId = generatedSaleId,
                    clientId = sale.clientId, // <--- REPASSANDO O ID DO CLIENTE
                    amountPaid = 0.0
                )
            }

            // 4. Preparar a Venda (Sale)
            val finalSale = sale.copy(
                id = generatedSaleId,
                sellerDeviceId = deviceId,
                date = creationTime,
                installments = finalInstallments
            )

            // 5. Persistência
            saleRepository.createSale(finalSale)
            installmentRepository.createInstallments(finalInstallments)

            Result.success(Unit)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
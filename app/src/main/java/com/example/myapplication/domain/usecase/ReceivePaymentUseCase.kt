package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.Installment
import com.example.myapplication.domain.repository.InstallmentRepository
import javax.inject.Inject

class ReceivePaymentUseCase @Inject constructor(
    private val repository: InstallmentRepository
) {
    /**
     * @param installment A parcela que está sendo paga
     * @param amountPaid O valor em dinheiro que o cobrador recebeu AGORA (ex: 50.00)
     */
    suspend operator fun invoke(installment: Installment, amountReceived: Double): Result<Unit> {
        return try {
            if (amountReceived <= 0) {
                return Result.failure(Exception("O valor recebido deve ser maior que zero."))
            }

            // Lógica do Acumulado:
            // O novo valor pago é o que já tinha sido pago antes + o que recebeu agora.
            val newTotalPaid = installment.amountPaid + amountReceived

            // Trava de segurança: Não deixar pagar mais que o valor original
            // (A menos que você implemente a lógica de amortização da última parcela depois)
            if (newTotalPaid > installment.originalValue + 0.01) { // +0.01 margem de erro double
                return Result.failure(Exception("Valor excede o restante da parcela. Use a opção de amortização."))
            }

            val updatedInstallment = installment.copy(
                amountPaid = newTotalPaid,
                paymentDate = System.currentTimeMillis() // Registra a data do pagamento
            )

            repository.updateInstallment(updatedInstallment)
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
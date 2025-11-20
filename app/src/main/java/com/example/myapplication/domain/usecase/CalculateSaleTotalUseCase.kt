package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.SaleItem
import javax.inject.Inject

class CalculateSaleTotalUseCase @Inject constructor() {

    operator fun invoke(items: List<SaleItem>): Double {
        return items.sumOf { it.quantity * it.unitPrice }
    }
}
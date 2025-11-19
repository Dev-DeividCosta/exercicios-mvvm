// File: app/src/main/java/com/example/myapplication/domain/usecase/GetProductsUseCase.kt

package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.Product
import com.example.myapplication.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de Uso responsável por obter a lista de todos os produtos do estoque.
 * Depende da interface [ProductRepository] para obter os dados.
 */
class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository // Injetado pelo Hilt
) {
    /**
     * O operador 'invoke' permite chamar o Use Case como uma função: getProductsUseCase().
     * @return Um Flow<List<Product>>
     */
    operator fun invoke(): Flow<List<Product>> {
        // Nenhuma regra de negócio aqui, apenas chamando o repositório
        return repository.getAllProducts()
    }
}
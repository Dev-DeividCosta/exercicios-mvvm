package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.Sale
import com.example.myapplication.domain.repository.ProductRepository
import com.example.myapplication.domain.repository.SaleRepository
import javax.inject.Inject
import com.example.myapplication.domain.model.Product
import java.lang.IllegalArgumentException

class CreateSaleUseCase @Inject constructor(
    private val saleRepository: SaleRepository,
    private val productRepository: ProductRepository // Usa a função suspensa para buscar o preço
) {
    /**
     * Lógica de Criação de Venda, garantindo que o preço do produto seja buscado.
     *
     * @param selectedProductId O ID real do produto selecionado.
     * @param clientName Nome do cliente (em sistemas reais, seria o clientId).
     * @param quantity Quantidade vendida (Hardcoded para 1, mas idealmente viria da UI).
     * @param installments Quantidade de parcelas.
     * @param observation Observação da venda.
     * @return O objeto Sale criado.
     * @throws NoSuchElementException se o produto não for encontrado no banco de dados.
     * @throws IllegalArgumentException se o ID do produto for inválido.
     */
    suspend fun execute(
        selectedProductId: String,
        clientName: String,
        quantity: Int = 1, // Usando 1 como padrão por enquanto
        installments: Int,
        observation: String
    ): Sale {
        if (selectedProductId.isBlank()) {
            throw IllegalArgumentException("O ID do produto não pode ser vazio.")
        }

        // 1. BUSCA DE DADOS (usando o método SUSPENDO corrigido)
        val product = productRepository.getProductByIdSync(selectedProductId)

        if (product == null) {
            throw NoSuchElementException("Produto com ID $selectedProductId não encontrado. Não é possível criar a venda.")
        }

        // 2. LÓGICA DE NEGÓCIO: Cálculo de Valores
        val totalValue = product.price * quantity

        // 3. CRIAÇÃO DO OBJETO SALE
        val sale = Sale(
            id = "", // O ID será preenchido pelo Repositório/Firestore
            clientId = "temp_client_id", // Placeholder
            clientName = clientName,
            productId = product.id,
            productName = product.name,
            quantity = quantity,
            installments = installments,
            totalValue = totalValue,
            observation = observation,
            date = System.currentTimeMillis()
        )

        // 4. PERSISTÊNCIA (Salvando no Repositório)
        saleRepository.createSale(sale)

        // 5. LÓGICA DE ESTOQUE (Se fosse implementada, seria aqui)
        /*
        // Exemplo:
        val newStock = product.stockQuantity - quantity
        productRepository.updateStock(product.id, newStock)
        */

        return sale
    }
}
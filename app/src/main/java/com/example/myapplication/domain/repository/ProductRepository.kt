package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun createProduct(product: Product)
    fun getAllProducts(): Flow<List<Product>>
    fun getProductById(productId: String): Flow<Product?>
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(productId: String)
    suspend fun getProductByIdSync(productId: String): Product?
}

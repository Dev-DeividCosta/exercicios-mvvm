package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.Product
import com.example.myapplication.domain.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductRepository {

    private val productsCollection = firestore.collection("products")

    override suspend fun createProduct(product: Product) {
        try {
            val docRef = productsCollection.document()
            docRef.set(product.copy(id = docRef.id)).await()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getAllProducts(): Flow<List<Product>> = callbackFlow {
        val snapshotListener = productsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val products = snapshot.toObjects(Product::class.java)
                trySend(products)
            }
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getProductById(productId: String): Flow<Product?> = callbackFlow {
        val docRef = productsCollection.document(productId)

        val snapshotListener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val product = snapshot.toObject(Product::class.java)
                trySend(product)
            } else {
                trySend(null)
            }
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun updateProduct(product: Product) {
        if (product.id.isBlank()) {
            throw IllegalArgumentException("Product ID is missing. Cannot update.")
        }
        try {
            productsCollection.document(product.id).set(product).await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteProduct(productId: String) {
        if (productId.isBlank()) {
            throw IllegalArgumentException("Product ID is missing. Cannot delete.")
        }
        try {
            productsCollection.document(productId).delete().await()
        } catch (e: Exception) {
            throw e
        }
    }
}

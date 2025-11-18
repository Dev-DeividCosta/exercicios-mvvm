package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.Sale
import com.example.myapplication.domain.repository.SaleRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SaleRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : SaleRepository {

    private val collection = firestore.collection("sales")

    override suspend fun createSale(sale: Sale) {
        try {
            val docRef = collection.document()
            val newSale = sale.copy(id = docRef.id)
            docRef.set(newSale).await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun getAllSales(): Flow<List<Sale>> = callbackFlow {
        val listener = collection
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                snapshot?.let { trySend(it.toObjects(Sale::class.java)) }
            }
        awaitClose { listener.remove() }
    }

    override fun getSalesByClientId(clientId: String): Flow<List<Sale>> = callbackFlow {
        val listener = collection
            .whereEqualTo("clientId", clientId)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                snapshot?.let { trySend(it.toObjects(Sale::class.java)) }
            }
        awaitClose { listener.remove() }
    }

    override fun getSaleById(saleId: String): Flow<Sale?> = callbackFlow {
        val listener = collection.document(saleId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                trySend(snapshot.toObject(Sale::class.java))
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    override fun getSalesBySellerId(sellerId: String): Flow<List<Sale>> = callbackFlow {
        val listener = collection
            .whereEqualTo("sellerId", sellerId)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                snapshot?.let { trySend(it.toObjects(Sale::class.java)) }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun updateSale(sale: Sale) {
        try {
            collection.document(sale.id).set(sale).await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteSale(saleId: String) {
        try {
            collection.document(saleId).delete().await()
        } catch (e: Exception) {
            throw e
        }
    }
}

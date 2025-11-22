package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.Installment
import com.example.myapplication.domain.repository.InstallmentRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class InstallmentRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : InstallmentRepository {

    private val collection = firestore.collection("installments")

    // Uso de BATCH para salvar todas as parcelas de uma vez
    override suspend fun createInstallments(installments: List<Installment>) {
        try {
            val batch = firestore.batch()
            installments.forEach { installment ->
                // Se o ID vier vazio, geramos um novo, senão usamos o que veio
                val docRef = if (installment.id.isBlank()) {
                    collection.document()
                } else {
                    collection.document(installment.id)
                }

                // Garante que o objeto salvo tenha o ID do documento
                val finalInstallment = installment.copy(id = docRef.id)
                batch.set(docRef, finalInstallment)
            }
            batch.commit().await() // Aguarda a conclusão do lote
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun getInstallmentsBySaleId(saleId: String): Flow<List<Installment>> = callbackFlow {
        val listener = collection
            .whereEqualTo("saleId", saleId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val installments = snapshot.toObjects(Installment::class.java)
                    // Opcional: Ordenar por número da parcela
                    trySend(installments.sortedBy { it.number })
                }
            }
        awaitClose { listener.remove() }
    }

    // Query assumindo que existe um campo "clientId" no documento do Firestore
    // (Denormalização recomendada para performance)
    override fun getInstallmentsByClientId(clientId: String): Flow<List<Installment>> = callbackFlow {
        val listener = collection
            .whereEqualTo("clientId", clientId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val installments = snapshot.toObjects(Installment::class.java)
                    trySend(installments.sortedBy { it.dueDate })
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun updateInstallment(installment: Installment) {
        try {
            collection.document(installment.id).set(installment)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteInstallmentsBySaleId(saleId: String) {
        try {
            // Primeiro busca os documentos
            val snapshot = collection.whereEqualTo("saleId", saleId).get().await()

            // Depois deleta em Batch
            val batch = firestore.batch()
            snapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            batch.commit().await()
        } catch (e: Exception) {
            throw e
        }
    }
}
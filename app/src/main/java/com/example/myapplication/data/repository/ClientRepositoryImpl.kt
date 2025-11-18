package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.Client
import com.example.myapplication.domain.repository.ClientRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ClientRepository {

    private val collection = firestore.collection("clients")

    override suspend fun createClient(client: Client) {
        try {
            // Cria uma referência de documento vazia para gerar o ID
            val docRef = collection.document()

            // Copia o cliente inserindo o ID gerado
            // O objeto Address dentro dele será salvo automaticamente pelo Firestore
            val newClient = client.copy(id = docRef.id)

            // Salva no banco
            docRef.set(newClient).await()
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun getAllClients(): Flow<List<Client>> = callbackFlow {
        // O listener garante atualização em tempo real
        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error) // Fecha o fluxo em caso de erro
                return@addSnapshotListener
            }

            if (snapshot != null) {
                // O Firestore converte o JSON para a classe Client (e Address internamente)
                val clients = snapshot.toObjects(Client::class.java)
                trySend(clients)
            }
        }
        awaitClose { listener.remove() }
    }

    override fun getClientById(clientId: String): Flow<Client?> = callbackFlow {
        val listener = collection.document(clientId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                trySend(snapshot.toObject(Client::class.java))
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun updateClient(client: Client) {
        try {
            // Isso atualiza tudo, inclusive se o endereço tiver mudado
            collection.document(client.id).set(client).await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteClient(clientId: String) {
        try {
            collection.document(clientId).delete().await()
        } catch (e: Exception) {
            throw e
        }
    }
}
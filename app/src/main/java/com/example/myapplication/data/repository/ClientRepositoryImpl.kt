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

    /**
     * Cria um novo cliente no Firestore.
     * REMOVEMOS O .await() para garantir que retorne imediatamente (offline-first).
     */
    override suspend fun createClient(client: Client) {
        try {
            // Cria uma referência de documento vazia para gerar o ID
            val docRef = collection.document()

            // Copia o cliente inserindo o ID gerado
            val newClient = client.copy(id = docRef.id)

            // Salva no banco e retorna IMEDIATAMENTE.
            // O Firestore fará a sincronização em background.
            docRef.set(newClient)
        } catch (e: Exception) {
            e.printStackTrace()
            // Isso só capturará erros de escrita local (ex: permissão negada localmente).
            throw e
        }
    }

    /**
     * Obtém todos os clientes em tempo real (Mantido o .addSnapshotListener).
     */
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

    /**
     * Obtém um cliente específico em tempo real (Mantido o .addSnapshotListener).
     */
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

    /**
     * Atualiza um cliente existente.
     * REMOVEMOS O .await() para garantir que retorne imediatamente (offline-first).
     */
    override suspend fun updateClient(client: Client) {
        try {
            // Isso atualiza tudo. Removemos o .await()
            collection.document(client.id).set(client)
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Deleta um cliente existente.
     * REMOVEMOS O .await() para garantir que retorne imediatamente (offline-first).
     */
    override suspend fun deleteClient(clientId: String) {
        try {
            // Removemos o .await()
            collection.document(clientId).delete()
        } catch (e: Exception) {
            throw e
        }
    }
}
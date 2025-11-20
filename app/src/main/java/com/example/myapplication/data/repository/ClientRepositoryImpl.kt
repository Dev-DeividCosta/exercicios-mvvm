package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.Client
import com.example.myapplication.domain.repository.ClientRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ClientRepository {

    private val collection = firestore.collection("clients")

    override suspend fun createClient(client: Client) {
        try {
            val docRef = collection.document()
            val newClient = client.copy(id = docRef.id)
            docRef.set(newClient)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun getAllClients(): Flow<List<Client>> = callbackFlow {
        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
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
            collection.document(client.id).set(client)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteClient(clientId: String) {
        try {
            collection.document(clientId).delete()
        } catch (e: Exception) {
            throw e
        }
    }
}

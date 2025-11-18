package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    private val collection = firestore.collection("users")

    override suspend fun saveUser(user: User) {
        try {
            if (user.id.isNotBlank()) {
                // Aqui usamos o UID do Auth como ID do documento
                collection.document(user.id).set(user).await()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getUserById(userId: String): Flow<User?> = callbackFlow {
        val listener = collection.document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                trySend(snapshot.toObject(User::class.java))
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }
}
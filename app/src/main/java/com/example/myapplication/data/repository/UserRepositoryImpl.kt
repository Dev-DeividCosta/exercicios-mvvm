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

    override suspend fun createUser(user: User) {
        try {
            val docRef = if (user.id.isNotBlank()) {
                collection.document(user.id)
            } else {
                collection.document()
            }
            val userToSave = user.copy(id = docRef.id)
            docRef.set(userToSave).await()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getAllUsers(): Flow<List<User>> = callbackFlow {
        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            snapshot?.let {
                trySend(it.toObjects(User::class.java))
            }
        }
        awaitClose { listener.remove() }
    }

    override fun getUsersByRole(role: String): Flow<List<User>> = callbackFlow {
        val listener = collection
            .whereEqualTo("role", role)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                snapshot?.let {
                    trySend(it.toObjects(User::class.java))
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun updateUser(user: User) {
        try {
            if (user.id.isBlank()) throw IllegalArgumentException("User ID is missing")
            collection.document(user.id).set(user).await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteUser(userId: String) {
        try {
            collection.document(userId).delete().await()
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

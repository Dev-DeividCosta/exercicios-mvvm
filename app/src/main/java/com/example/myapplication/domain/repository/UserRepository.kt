package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(user: User)
    fun getAllUsers(): Flow<List<User>>
    fun getUserById(userId: String): Flow<User?>
    fun getUsersByRole(role: String): Flow<List<User>> // Ex: "VENDEDOR"
    suspend fun updateUser(user: User)
    suspend fun deleteUser(userId: String)
}
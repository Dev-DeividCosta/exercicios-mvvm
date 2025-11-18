package com.example.myapplication.di

import com.example.myapplication.data.repository.ClientRepositoryImpl
import com.example.myapplication.data.repository.ProductRepositoryImpl
import com.example.myapplication.data.repository.SaleRepositoryImpl
import com.example.myapplication.data.repository.UserRepositoryImpl
import com.example.myapplication.domain.repository.ClientRepository
import com.example.myapplication.domain.repository.ProductRepository
import com.example.myapplication.domain.repository.SaleRepository
import com.example.myapplication.domain.repository.UserRepository
import com.example.padrecicero.domain.repository.ClientRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // 1. Provê a instância única do Firestore para o app todo
    @Provides
    @Singleton
    fun provideFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    // 2. Provê o ProductRepository
    @Provides
    @Singleton
    fun provideProductRepository(
        firestore: FirebaseFirestore
    ): ProductRepository {
        return ProductRepositoryImpl(firestore = firestore)
    }

    // 3. Provê o ClientRepository (NOVO)
    @Provides
    @Singleton
    fun provideClientRepository(
        firestore: FirebaseFirestore
    ): ClientRepository {
        return ClientRepositoryImpl(firestore = firestore)
    }

    // 4. Provê o SaleRepository (NOVO)
    @Provides
    @Singleton
    fun provideSaleRepository(
        firestore: FirebaseFirestore
    ): SaleRepository {
        return SaleRepositoryImpl(firestore = firestore)
    }

    // 5. Provê o UserRepository (NOVO)
    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore
    ): UserRepository {
        return UserRepositoryImpl(firestore = firestore)
    }
}
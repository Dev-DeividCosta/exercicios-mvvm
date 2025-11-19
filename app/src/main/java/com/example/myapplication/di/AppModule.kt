package com.example.myapplication.di

import com.example.myapplication.data.repository.ClientRepositoryImpl
import com.example.myapplication.data.repository.ProductRepositoryImpl
import com.example.myapplication.data.repository.SaleRepositoryImpl
import com.example.myapplication.data.repository.UserRepositoryImpl
import com.example.myapplication.domain.repository.ClientRepository
import com.example.myapplication.domain.repository.ProductRepository
import com.example.myapplication.domain.repository.SaleRepository
import com.example.myapplication.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Firestore com modo Offline habilitado
    @Provides
    @Singleton
    fun provideFirestoreInstance(): FirebaseFirestore {
        val db = FirebaseFirestore.getInstance()

        db.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true) // ESSENCIAL
            .build()

        return db
    }

    // ProductRepository
    @Provides
    @Singleton
    fun provideProductRepository(
        firestore: FirebaseFirestore
    ): ProductRepository {
        return ProductRepositoryImpl(firestore = firestore)
    }

    // ClientRepository
    @Provides
    @Singleton
    fun provideClientRepository(
        firestore: FirebaseFirestore
    ): ClientRepository {
        return ClientRepositoryImpl(firestore = firestore)
    }

    // SaleRepository
    @Provides
    @Singleton
    fun provideSaleRepository(
        firestore: FirebaseFirestore
    ): SaleRepository {
        return SaleRepositoryImpl(firestore = firestore)
    }

    // UserRepository
    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore
    ): UserRepository {
        return UserRepositoryImpl(firestore = firestore)
    }
}

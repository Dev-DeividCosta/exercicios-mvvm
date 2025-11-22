////package com.example.myapplication.di
////
////import com.example.myapplication.data.repository.ClientRepositoryImpl
////import com.example.myapplication.data.repository.ProductRepositoryImpl
////import com.example.myapplication.data.repository.SaleRepositoryImpl
////import com.example.myapplication.data.repository.UserRepositoryImpl
////import com.example.myapplication.domain.repository.ClientRepository
////import com.example.myapplication.domain.repository.ProductRepository
////import com.example.myapplication.domain.repository.SaleRepository
////import com.example.myapplication.domain.repository.UserRepository
////import com.google.firebase.firestore.FirebaseFirestore
////import com.google.firebase.firestore.FirebaseFirestoreSettings
////import dagger.Module
////import dagger.Provides
////import dagger.hilt.InstallIn
////import dagger.hilt.components.SingletonComponent
////import javax.inject.Singleton
////
////@Module
////@InstallIn(SingletonComponent::class)
////object AppModule {
////
////    // Firestore com modo Offline habilitado
////    @Provides
////    @Singleton
////    fun provideFirestoreInstance(): FirebaseFirestore {
////        val db = FirebaseFirestore.getInstance()
////
////        db.firestoreSettings = FirebaseFirestoreSettings.Builder()
////            .setPersistenceEnabled(true) // ESSENCIAL
////            .build()
////
////        return db
////    }
////
////    // ProductRepository
////    @Provides
////    @Singleton
////    fun provideProductRepository(
////        firestore: FirebaseFirestore
////    ): ProductRepository {
////        return ProductRepositoryImpl(firestore = firestore)
////    }
////
////    // ClientRepository
////    @Provides
////    @Singleton
////    fun provideClientRepository(
////        firestore: FirebaseFirestore
////    ): ClientRepository {
////        return ClientRepositoryImpl(firestore = firestore)
////    }
////
////    // SaleRepository
////    @Provides
////    @Singleton
////    fun provideSaleRepository(
////        firestore: FirebaseFirestore
////    ): SaleRepository {
////        return SaleRepositoryImpl(firestore = firestore)
////    }
////
////    // UserRepository
////    @Provides
////    @Singleton
////    fun provideUserRepository(
////        firestore: FirebaseFirestore
////    ): UserRepository {
////        return UserRepositoryImpl(firestore = firestore)
////    }
////}
//
//
//package com.example.myapplication.di
//
//import android.content.Context
//import com.example.myapplication.data.provider.DeviceIdProviderImpl
//import com.example.myapplication.data.repository.ClientRepositoryImpl
//import com.example.myapplication.data.repository.ProductRepositoryImpl
//import com.example.myapplication.data.repository.SaleRepositoryImpl
//import com.example.myapplication.data.repository.UserRepositoryImpl
//import com.example.myapplication.domain.provider.DeviceIdProvider
//import com.example.myapplication.domain.repository.ClientRepository
//import com.example.myapplication.domain.repository.ProductRepository
//import com.example.myapplication.domain.repository.SaleRepository
//import com.example.myapplication.domain.repository.UserRepository
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.FirebaseFirestoreSettings
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//
//    // Firestore com modo Offline habilitado
//    @Provides
//    @Singleton
//    fun provideFirestoreInstance(): FirebaseFirestore {
//        val db = FirebaseFirestore.getInstance()
//
//        db.firestoreSettings = FirebaseFirestoreSettings.Builder()
//            .setPersistenceEnabled(true) // ESSENCIAL
//            .build()
//
//        return db
//    }
//
//    // ProductRepository
//    @Provides
//    @Singleton
//    fun provideProductRepository(
//        firestore: FirebaseFirestore
//    ): ProductRepository {
//        return ProductRepositoryImpl(firestore = firestore)
//    }
//
//    // ClientRepository
//    @Provides
//    @Singleton
//    fun provideClientRepository(
//        firestore: FirebaseFirestore
//    ): ClientRepository {
//        return ClientRepositoryImpl(firestore = firestore)
//    }
//
//    // SaleRepository
//    @Provides
//    @Singleton
//    fun provideSaleRepository(
//        firestore: FirebaseFirestore
//    ): SaleRepository {
//        return SaleRepositoryImpl(firestore = firestore)
//    }
//
//    // UserRepository
//    @Provides
//    @Singleton
//    fun provideUserRepository(
//        firestore: FirebaseFirestore
//    ): UserRepository {
//        return UserRepositoryImpl(firestore = firestore)
//    }
//
//    // --- NOVO: DeviceIdProvider (Resolve o erro de MissingBinding) ---
//    @Provides
//    @Singleton
//    fun provideDeviceIdProvider(
//        @ApplicationContext context: Context
//    ): DeviceIdProvider {
//        return DeviceIdProviderImpl(context)
//    }
//}

package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.data.provider.DeviceIdProviderImpl
import com.example.myapplication.data.repository.ClientRepositoryImpl
import com.example.myapplication.data.repository.InstallmentRepositoryImpl
import com.example.myapplication.data.repository.ProductRepositoryImpl
import com.example.myapplication.data.repository.SaleRepositoryImpl
import com.example.myapplication.data.repository.UserRepositoryImpl
import com.example.myapplication.domain.provider.DeviceIdProvider
import com.example.myapplication.domain.repository.ClientRepository
import com.example.myapplication.domain.repository.InstallmentRepository
import com.example.myapplication.domain.repository.ProductRepository
import com.example.myapplication.domain.repository.SaleRepository
import com.example.myapplication.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    // --- ADICIONADO AGORA: InstallmentRepository (Correção do Erro) ---
    @Provides
    @Singleton
    fun provideInstallmentRepository(
        firestore: FirebaseFirestore
    ): InstallmentRepository {
        return InstallmentRepositoryImpl(firestore = firestore)
    }

    // UserRepository
    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore
    ): UserRepository {
        return UserRepositoryImpl(firestore = firestore)
    }

    // DeviceIdProvider
    @Provides
    @Singleton
    fun provideDeviceIdProvider(
        @ApplicationContext context: Context
    ): DeviceIdProvider {
        return DeviceIdProviderImpl(context)
    }
}
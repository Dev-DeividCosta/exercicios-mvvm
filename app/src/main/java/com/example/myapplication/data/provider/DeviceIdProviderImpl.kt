package com.example.myapplication.data.provider

import android.content.Context
import android.provider.Settings
import com.example.myapplication.domain.provider.DeviceIdProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeviceIdProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DeviceIdProvider {

    override fun getDeviceId(): String {
        // Pega o ANDROID_ID, que é único para o usuário no dispositivo
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown_device"
    }
}
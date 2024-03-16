package org.pw.mateuszkrawczuk.bluetoothmqttproxy.di

import android.content.Context
import com.polidea.rxandroidble3.RxBleClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BluetoothModule {

    @Provides
    @Singleton
    fun provideBluetoothClient(@ApplicationContext context: Context): RxBleClient {
        return RxBleClient.create(context)
    }
}

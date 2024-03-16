package org.pw.mateuszkrawczuk.bluetoothmqttproxy

import android.app.Application
import android.util.Log
import com.polidea.rxandroidble3.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class MqttApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
package org.pw.mateuszkrawczuk.bluetoothmqttproxy.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.polidea.rxandroidble3.RxBleClient
import javax.inject.Inject


class MainActivityViewModel @Inject constructor (val rxBleClient: RxBleClient ) : ViewModel() {

    fun scan(){

        val x =  rxBleClient.scanBleDevices()

        Log.i("MainActivityViewModel", "scan: $x")
        x.subscribe(
            { scanResult ->
                // Process scan result here.
                Log.i("MainActivityViewModel", "scan: $scanResult")
            },
            { throwable ->
                // Handle an error here.
                Log.i("MainActivityViewModel", "scan: $throwable")
            }
        )
    }
}
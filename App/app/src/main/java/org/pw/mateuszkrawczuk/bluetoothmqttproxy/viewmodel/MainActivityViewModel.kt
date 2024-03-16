package org.pw.mateuszkrawczuk.bluetoothmqttproxy.viewmodel

import androidx.lifecycle.ViewModel
import com.polidea.rxandroidble3.RxBleClient
import timber.log.Timber
import javax.inject.Inject


class MainActivityViewModel @Inject constructor (val rxBleClient: RxBleClient ) : ViewModel() {


    fun scan(){

        val x =  rxBleClient.scanBleDevices()

        Timber.i( "scan: $x")
        x.subscribe(
            { scanResult ->
                // Process scan result here.
                Timber.i("scan: $scanResult")

            },
            { throwable ->
                // Handle an error here.
                Timber.i( "scan: $throwable")
            }
        )
    }
}
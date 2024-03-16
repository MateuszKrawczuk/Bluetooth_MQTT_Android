package org.pw.mateuszkrawczuk.bluetoothmqttproxy.viewmodel

import androidx.lifecycle.ViewModel
import com.polidea.rxandroidble3.RxBleClient
import javax.inject.Inject


class MainActivityViewModel @Inject constructor (val rxBleClient: RxBleClient ) : ViewModel() {

    fun scan(){
        rxBleClient.scanBleDevices()
    }
}
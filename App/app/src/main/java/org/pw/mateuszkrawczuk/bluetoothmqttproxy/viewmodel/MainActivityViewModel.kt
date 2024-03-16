package org.pw.mateuszkrawczuk.bluetoothmqttproxy.viewmodel


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polidea.rxandroidble3.RxBleClient
import com.polidea.rxandroidble3.scan.ScanFilter
import com.polidea.rxandroidble3.scan.ScanSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.pw.mateuszkrawczuk.bluetoothmqttproxy.model.BluetoothDevice
import timber.log.Timber
import javax.inject.Inject


class MainActivityViewModel @Inject constructor (val rxBleClient: RxBleClient ) : ViewModel() {

    val deviceList = mutableStateListOf<BluetoothDevice>()
    val scanCounter = mutableStateOf(0)

    fun scan() {
        viewModelScope.launch {
            _scan()
        }
    }

    fun removeDevice(device: BluetoothDevice) {
        deviceList.remove(device)
    }

    fun cleanDeviceList() {
        deviceList.clear()
    }

    /**
     * Scans for BLE devices
     *
     */
    suspend fun _scan() {
        Timber.i("Prepare to scan...")
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
        val scanFilter: ScanFilter = ScanFilter.Builder().build()
        val x = rxBleClient.scanBleDevices(scanSettings, scanFilter)
        Timber.i( "scan: $x")
        val result = x.subscribe(
            { scanResult ->
                // Process scan result here.
                Timber.i("scan: $scanResult")
                Log.i("", "scan: $scanResult")
                val newDevice = BluetoothDevice(
                    scanResult.bleDevice.name ?: "Unknown",
                    scanResult.bleDevice.macAddress
                )
                val existingDevice = deviceList.find { it.address == newDevice.address }

                if (existingDevice == null) {
                    // If the device does not exist in the list, add it
                    deviceList.add(newDevice)
                } else if (existingDevice.name == "Unknown" && newDevice.name != "Unknown") {
                    // If the device exists and its name is "Unknown", update the name
                    val updatedDevice = existingDevice.copy(name = newDevice.name)
                    deviceList[deviceList.indexOf(existingDevice)] = updatedDevice
                }
            },
            { throwable ->
                // Handle an error here.
                Timber.i( "scan: $throwable")
            }
        )
        scanCounter.value = 5
        while (scanCounter.value > 0) {
            delay(1000)
            scanCounter.value -= 1
        }
        result.dispose()

    }
}
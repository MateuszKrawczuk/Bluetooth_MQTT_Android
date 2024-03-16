package org.pw.mateuszkrawczuk.bluetoothmqttproxy.model

data class BluetoothDevice(
    val name: String,
    val address: String,
) {
    override fun toString(): String {
        return "BluetoothDevice(name='$name', address='$address')"
    }
}
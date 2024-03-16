package org.pw.mateuszkrawczuk.bluetoothmqttproxy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import org.pw.mateuszkrawczuk.bluetoothmqttproxy.model.BluetoothDevice
import org.pw.mateuszkrawczuk.bluetoothmqttproxy.ui.theme.BluetoothMQTTProxyTheme
import org.pw.mateuszkrawczuk.bluetoothmqttproxy.viewmodel.MainActivityViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BluetoothMQTTProxyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Sample(viewModel)
                }
            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Sample(viewModel: MainActivityViewModel) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.BLUETOOTH_CONNECT,
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text("Thanks! I can access your exact location.")
            Text("Devices found: ${viewModel.deviceList.size}")
            Button(onClick = { viewModel.scan() }) {
                Text("Scan")

            }
            Button(onClick = { viewModel.cleanDeviceList() }) {
                Text("Clean")
            }
            if (viewModel.deviceList.isEmpty()) {
                Text("No devices found")
            } else {
                DeviceList(viewModel)
            }

            Text("Scanning for: ${viewModel.scanCounter.value} seconds")

        }

    } else {
        Column {
            val allPermissionsRevoked =
                locationPermissionsState.permissions.size ==
                        locationPermissionsState.revokedPermissions.size

            val textToShow = if (!allPermissionsRevoked) {
                // If not all the permissions are revoked, it's because the user accepted the COARSE
                // location permission, but not the FINE one.
                "Yay! Thanks for letting me access your approximate location. " +
                        "But you know what would be great? If you allow me to know where you " +
                        "exactly are. Thank you!"
            } else if (locationPermissionsState.shouldShowRationale) {
                // Both location permissions have been denied
                "Getting your exact location is important for this app. " +
                        "Please grant us fine location. Thank you :D"
            } else {
                // First time the user sees this feature or the user doesn't want to be asked again
                "This feature requires location permission"
            }

            val buttonText = if (!allPermissionsRevoked) {
                "Allow precise location"
            } else {
                "Request permissions"
            }

            Text(text = textToShow)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
                Text(buttonText)
            }
        }
    }
}

@Composable
private fun DeviceList(viewModel: MainActivityViewModel) {
    Column {
        viewModel.deviceList.forEach {
            BleDevice(it)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun BleDevice(device: BluetoothDevice) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Name: ${device.name}")
            Text("Address: ${device.address}")
        }
    }

}
package com.ayoo.consumer.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSelectionScreen(navController: NavController, initialAddress: String) {
    var searchQuery by remember { mutableStateOf(initialAddress) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current
    var locationInfo by remember { mutableStateOf(initialAddress) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                getCurrentLocation(context) { address ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "selectedAddress",
                        address
                    )
                    navController.popBackStack()
                }
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Deliver to") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        trailingIcon = {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = "Search by image"
                            )
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            Button(
                onClick = { navController.navigate("map_pinpoint") },
                modifier = Modifier.padding(horizontal = 16.dp),
                shape = RoundedCornerShape(50)
            ) {
                Icon(
                    Icons.Default.Map,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Choose on Ayoo MyFind")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Recent") })
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Suggested") })
                Tab(
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 },
                    text = { Text("Saved") })
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Current Location Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.GpsFixed,
                    contentDescription = "Current Location",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Current location", fontWeight = FontWeight.Bold)
                    Text(locationInfo, style = MaterialTheme.typography.bodySmall)
                }
                Icon(Icons.Default.MoreVert, contentDescription = "More options")
            }
            Divider(modifier = Modifier.padding(vertical = 16.dp))
        }
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(context: Context, onAddressFetched: (String) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(context, Locale.getDefault())
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        ) { addresses ->
                            val address =
                                addresses.firstOrNull()?.getAddressLine(0) ?: "Address not found"
                            onAddressFetched(address)
                        }
                    } else {
                        @Suppress("DEPRECATION")
                        val addresses =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val address =
                            addresses?.firstOrNull()?.getAddressLine(0) ?: "Address not found"
                        onAddressFetched(address)
                    }
                } catch (e: Exception) {
                    onAddressFetched("Could not determine address")
                }
            } else {
                Toast.makeText(context, "Could not get location.", Toast.LENGTH_SHORT).show()
                onAddressFetched("Could not get location")
            }
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to get location.", Toast.LENGTH_SHORT).show()
            onAddressFetched("Failed to get location")
        }
}

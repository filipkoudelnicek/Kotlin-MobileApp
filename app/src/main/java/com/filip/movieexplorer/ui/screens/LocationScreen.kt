package com.filip.movieexplorer.ui.screens

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.filip.movieexplorer.ui.location.LocationUiState
import com.filip.movieexplorer.ui.location.LocationViewModel

@Composable
fun LocationRoute(
    onNavigateBack: () -> Unit
) {
    val app = LocalContext.current.applicationContext as Application
    val viewModel: LocationViewModel = viewModel(
        factory = LocationViewModel.provideFactory(app)
    )
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.any { it.value }
        viewModel.updatePermission(granted)
        if (granted) {
            viewModel.fetchLocation()
        }
    }

    LaunchedEffect(Unit) {
        val granted = hasLocationPermission(context)
        viewModel.updatePermission(granted)
        if (granted) {
            viewModel.fetchLocation()
        }
    }

    LocationScreen(
        state = uiState,
        onNavigateBack = onNavigateBack,
        onRequestPermission = {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        },
        onRefreshLocation = { viewModel.fetchLocation() }
    )
}

@Composable
fun LocationScreen(
    state: LocationUiState,
    onNavigateBack: () -> Unit,
    onRequestPermission: () -> Unit,
    onRefreshLocation: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aktuální poloha") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Přístup k poloze: ${if (state.hasPermission) "udělen" else "neudělen"}",
                style = MaterialTheme.typography.bodyMedium
            )

            state.location?.let { location ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Zeměpisná šířka: ${location.latitude}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Zeměpisná délka: ${location.longitude}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Čas získání: ${java.text.DateFormat.getDateTimeInstance().format(location.timestamp)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } ?: Text(
                text = "Poloha zatím není k dispozici.",
                style = MaterialTheme.typography.bodyMedium
            )

            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (!state.hasPermission) {
                Button(onClick = onRequestPermission) {
                    Text("Povolit přístup k poloze")
                }
            } else {
                Button(
                    onClick = onRefreshLocation,
                    enabled = !state.isLoading
                ) {
                    Text(if (state.isLoading) "Načítám..." else "Získat aktuální polohu")
                }
            }
        }
    }
}

private fun hasLocationPermission(context: android.content.Context): Boolean {
    val fineGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val coarseGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    return fineGranted || coarseGranted
}



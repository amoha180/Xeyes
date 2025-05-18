@file:OptIn(ExperimentalPermissionsApi::class)
package de.yanneckreiss.mlkittutorial.ui

import android.Manifest
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import de.yanneckreiss.mlkittutorial.ui.camera.CameraScreen
import de.yanneckreiss.mlkittutorial.ui.history.HistoryScreen
import de.yanneckreiss.mlkittutorial.ui.no_permission.NoPermissionScreen
import de.yanneckreiss.mlkittutorial.viewmodel.HistoryViewModel

@Composable
fun MainScreen() {
    // 1) CAMERA permission
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val granted = permissionState.status.isGranted
    LaunchedEffect(granted) {
        if (!granted) permissionState.launchPermissionRequest()
    }

    if (!granted) {
        NoPermissionScreen(onRequestPermission = permissionState::launchPermissionRequest)
    } else {
        // 2) Nav + ViewModel
        val navController = rememberNavController()
        val app = LocalContext.current.applicationContext as Application
        val historyVm: HistoryViewModel = viewModel(
            factory = HistoryViewModel.provideFactory(app)
        )
        val history by historyVm.historyList.collectAsState()

        NavHost(navController, startDestination = "home") {
            composable("home") {
                HomeScreen(
                    onLiveScan = { navController.navigate("camera") },
                    onHistory  = { navController.navigate("history") }
                )
            }
            composable("camera") {
                CameraScreen(
                    onBack       = { navController.popBackStack() },
                    onManualSnap = { text ->
                        if (text.isNotBlank()) historyVm.insert(text)
                    }
                )
            }
            composable("history") {
                val vm: HistoryViewModel = viewModel(
                    factory = HistoryViewModel.provideFactory(app)
                )
                HistoryScreen(
                    history  = vm.historyList.collectAsState().value,
                    onBack   = { navController.popBackStack() },
                    historyVm = vm
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview_MainScreen() {
    MainScreen()
}

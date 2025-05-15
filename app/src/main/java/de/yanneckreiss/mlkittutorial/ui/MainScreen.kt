@file:OptIn(ExperimentalPermissionsApi::class)
package de.yanneckreiss.mlkittutorial.ui

import android.Manifest
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.*
import androidx.navigation.compose.*
import de.yanneckreiss.mlkittutorial.ui.camera.CameraScreen
import de.yanneckreiss.mlkittutorial.ui.history.HistoryScreen
import de.yanneckreiss.mlkittutorial.ui.no_permission.NoPermissionScreen
import de.yanneckreiss.mlkittutorial.ui.HomeScreen

@Composable
fun MainScreen() {
    // 1) Set up the CAMERA permission state
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val granted = permissionState.status.isGranted

    // 2) As soon as we compose, if not granted, fire the OS dialog
    LaunchedEffect(Unit) {
        if (!granted) {
            permissionState.launchPermissionRequest()
        }
    }

    // 3) Show either the grant-permission UI or our Nav graph
    if (!granted) {
        NoPermissionScreen(onRequestPermission = permissionState::launchPermissionRequest)
    } else {
        val nav = rememberNavController()
        val history = remember { mutableStateListOf<String>() }

        NavHost(navController = nav, startDestination = "home") {
            composable("home") {
                HomeScreen(
                    onLiveScan = { nav.navigate("camera") },
                    onHistory  = { nav.navigate("history") }
                )
            }
            composable("camera") {
                CameraScreen(
                    onBack       = { nav.popBackStack() },
                    onManualSnap = { text ->
                        if (text.isNotBlank()) history.add(0, text)
                    }
                )
            }
            composable("history") {
                HistoryScreen(
                    history = history,
                    onBack  = { nav.popBackStack() }
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

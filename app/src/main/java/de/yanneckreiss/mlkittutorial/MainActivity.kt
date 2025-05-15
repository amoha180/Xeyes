package de.yanneckreiss.mlkittutorial

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import de.yanneckreiss.mlkittutorial.ui.MainScreen
import de.yanneckreiss.mlkittutorial.ui.theme.JetpackComposeMLKitTutorialTheme

class MainActivity : ComponentActivity() {

    // Launcher for the CAMERA permission request
    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            // You could react to denial here (e.g. show a Toast),
            // but we simply let MainScreen re-query ContextCompat below.
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ① If we don’t already have CAMERA, fire the OS dialog now.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }

        // ② Now set up your Compose UI (which can still check again if needed)
        setContent {
            JetpackComposeMLKitTutorialTheme {
                MainScreen()
            }
        }
    }
}

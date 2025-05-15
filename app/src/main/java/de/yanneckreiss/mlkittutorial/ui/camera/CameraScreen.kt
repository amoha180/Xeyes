package de.yanneckreiss.mlkittutorial.ui.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color as AndroidColor
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.AspectRatio
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.core.content.ContextCompat
import de.yanneckreiss.mlkittutorial.ui.camera.startTextRecognition
import de.yanneckreiss.mlkittutorial.ui.camera.TextRecognitionAnalyzer

@SuppressLint("ComposeModifierMissing")
@Composable
fun CameraScreen(
    onBack:       ()->Unit,
    onManualSnap: (recognizedText: String)->Unit
) {
    val context         = LocalContext.current
    val lifecycleOwner  = LocalLifecycleOwner.current as LifecycleOwner
    val cameraController = remember { LifecycleCameraController(context) }
    var detectedText by remember { mutableStateOf("No text detected yet..") }

    // ML Kit callback
    fun onTextUpdated(updatedText: String) {
        detectedText = updatedText
    }

    // Kick off the camera + analyzer
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setBackgroundColor(AndroidColor.BLACK)
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        startTextRecognition(
                            context                = ctx,
                            cameraController       = cameraController,
                            lifecycleOwner         = lifecycleOwner,
                            previewView            = previewView,
                            onDetectedTextUpdated  = ::onTextUpdated
                        )
                    }
                }
            )

            // ◾ Back button
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colors.surface.copy(alpha = 0.7f),
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            // ◾ Manual capture FAB
            FloatingActionButton(
                onClick = { onManualSnap(detectedText) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Camera, contentDescription = "Capture")
            }

            // ◾ Detected-text overlay
            Text(
                text = detectedText,
                color = androidx.compose.ui.graphics.Color.White,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .background(
                        androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.6f),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(8.dp)
            )
        }
    }
}
private fun startTextRecognition(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onDetectedTextUpdated: (String) -> Unit
) {
    // tell the controller to analyze 16:9 frames
    cameraController.imageAnalysisTargetSize =
        CameraController.OutputSize(AspectRatio.RATIO_16_9)

    // hook our ML-Kit analyzer
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        TextRecognitionAnalyzer(onDetectedTextUpdated = onDetectedTextUpdated)
    )

    // bind camera lifecycle and attach to PreviewView
    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}

# Xeyes

A sample Android application demonstrating how to build a live text-recognition app using Jetpack Compose and Google’s ML Kit.

## Features

* Live OCR of the camera preview
* Manual capture of recognized text
* Persistent capture history with inline editing and copy-to-clipboard
* Custom splash screen with branded color palette
* Simple, pale-teal & cream theme

## Prerequisites

• Android Studio Flamingo or later
• JDK 17 or newer
• Android SDK (compileSdkVersion = 34)
• Kotlin 1.9.x

## Getting Started

1. Clone the repository:
   `git clone  https://github.com/amoha180/Xeyes.git`
2. Open the project in Android Studio.
3. Agree to install any missing SDK components or plugins.
4. Sync Gradle and let all dependencies download.
5. Run on a physical device or emulator (minimum API 21).

## Configuration

• Grant CAMERA permission on first launch.
• To disable Android 12+’s built-in splash, add this to your app theme in `res/values/themes.xml`:
`<item name="android:windowSplashScreenEnabled">false</item>`
• Place your splash icon at `app/src/main/res/drawable/eye_icon.png`.

## Core Modules

• `CameraScreen.kt` – sets up CameraX + ML Kit analyzer and displays live OCR results in a scrollable overlay.
• `TextRecognitionAnalyzer.kt` – ImageAnalysis.Analyzer implementation that feeds preview frames into ML Kit.
• `HistoryScreen.kt` – shows your saved captures in cards; tap to edit or copy.
• `HistoryViewModel.kt` + `DataStore` – handles local persistence of capture history (add, update).
• `SplashScreen.kt` – Compose screen showing your logo over a teal background, then transitions to main UI.
• `ui/theme/Color.kt` + `Theme.kt` – custom light/dark theme using the eye-icon’s teal, cream, brown, highlight palette.

## Key Dependencies

implementation “androidx.camera\:camera-core:1.3.0-beta02”
implementation “androidx.camera\:camera-camera2:1.3.0-beta02”
implementation “androidx.camera\:camera-lifecycle:1.3.0-beta02”
implementation “androidx.camera\:camera-view:1.3.0-beta02”
implementation “com.google.mlkit\:vision-text-recognition-latin:17.0.6”
implementation “org.jetbrains.kotlinx\:kotlinx-coroutines-core:1.6.4”
implementation “org.jetbrains.kotlinx\:kotlinx-coroutines-android:1.6.4”
implementation “org.jetbrains.kotlinx\:kotlinx-coroutines-play-services:1.6.4”
implementation “androidx.compose.ui\:ui-graphics:1.4.3”

## Project Structure

• `MainActivity.kt` – requests camera permission, shows splash then main nav host.
• `ui/camera/CameraScreen.kt` – live scan UI.
• `ui/history/HistoryScreen.kt` – history list UI.
• `ui/splash/SplashScreen.kt` – initial branded splash.
• `ui/theme/` – color palette, shapes, typography, theme wrapper.
• `data/` – DataStore repository and models.
• `viewmodel/HistoryViewModel.kt` – exposes state flow of history to Compose.

## Usage

• Point your device camera at text; the recognized string appears in the bottom pane.
• Tap the camera FAB to save the current text to history.
• Open History from the navigation menu, tap a card to view or edit the full text, or copy it.

## Customization

• Adjust splash duration by changing `delay(2000)` in `SplashScreen.kt`.
• Tweak colors in `ui/theme/Color.kt` for your own palette.
• Modify the live-scan overlay size or position in `CameraScreen.kt`.

## License

This project is released under the MIT License. Feel free to use and adapt.

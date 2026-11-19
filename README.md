# Nispax File Explorer

A native Android file manager built with Kotlin and Jetpack Compose.

## Current alpha scope

- Black + Titanium + Electric Blue design system.
- Premium dashboard and five-section navigation.
- User-authorized folder access using Android Storage Access Framework.
- Basic browsing of the selected folder and its child directories.
- Offline-first foundation.

## Build

Requirements:
- JDK 17
- Android SDK Platform 35
- Gradle 8.11.1 or compatible

Run:

```bash
gradle assembleDebug
```

APK output:

```
app/build/outputs/apk/debug/app-debug.apk
```

## Storage permissions

Nispax uses the Storage Access Framework. The user selects a folder and grants access through Android's system picker. External USB storage is supported when exposed by Android through the document provider. The app does not request unrestricted all-files access.

## Repository

`cornerfood565-ctrl/Nispax-file-folder`

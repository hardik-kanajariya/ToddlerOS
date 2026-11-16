# ToddlerOS: Vasu's Play, Learn & Discover World

A 100% self-contained, offline-first Android Home Launcher with an integrated **Secure Child Mode & Kiosk Experience** exclusively for **Vasu**.

---

## Core Product Pillars

### 1. 100% Self-Contained Platform
- All learning activities, creative tools, games, and entertainment are built directly into native Android screens.
- Zero dependencies on external third-party applications.
- Zero network requests, remote web views, or cloud services.
- Completely private and offline-first.

### 2. Five Internal Discovery Hubs
- **Learn 🧩**:
  - *Alphabet Adventure*: Phonics, pronunciations, illustrations, and tap-to-repeat.
  - *Number Garden*: Visual counting of garden fruits, flowers, and stars.
  - *Animal World*: Illustrated animals, habitats, and sounds.
  - *Colors & Shapes*: Interactive discovery quests matching colors and shapes.
  - *Fruits & Veggies*: Fresh produce recognition, colors, and audio pronunciation.
  - *Vehicles & Transport*: Cars, trains, airplanes, boats, and rockets with sound effects.
- **Play 🚀**:
  - *Memory Matching*: Animal card-flip pair matching game.
  - *Shape Puzzle*: Interactive shape sorting and outline fitting.
- **Create 🎨**:
  - *Creative Studio*: Freehand drawing canvas with brush thicknesses, color palette, eraser, and local artwork saving.
  - *Photo Gallery*: Local gallery of saved drawings and family pictures.
- **Discover 🎵**:
  - *Vasu's Tunes*: Cheerful offline melodies and nursery rhymes.
  - *Vasu's Videos*: Offline children's video library with position resume.
  - *Interactive Stories*: Picture-book stories with offline read-aloud.
- **Parent Settings ⚙️**:
  - Child Mode & Kiosk activation/status.
  - Vasu's Learning Progress & Star achievements.
  - Educational content management via Storage Access Framework (SAF).
  - In-app spoken narration & sound toggle.
  - Shortcut to Android's default home app settings.

---

## Secure Child Mode & Kiosk Architecture

### Android Lock Task Mode
ToddlerOS integrates Android's official `DevicePolicyManager` Lock Task Mode. When Secure Child Mode is active:
- Notification drawer and Quick Settings access are restricted.
- System Home navigation and Overview / Recent Apps switching are prevented.
- Physical power and volume buttons remain fully operational.

### Password-Protected Parent Exit
Leaving Child Mode requires entering a 6-digit numeric password on the custom in-app keypad:
- **Default Development Password**: `123456`
- Structured to allow parent-configurable credentials in future releases.
- Password is validated securely in memory and never logged.

### Device Owner Provisioning (Optional, for strict Kiosk Mode)
To enforce strict kiosk lock without user-confirmation prompts:
1. Ensure the device has no Google or user accounts added (or is newly factory reset).
2. Enable USB Debugging and connect via ADB:
   ```bash
   adb shell dpm set-device-owner com.toddleros.launcher/.kiosk.ToddlerDeviceAdminReceiver
   ```
*(Note: If unprovisioned, ToddlerOS gracefully falls back to standard Android Screen Pinning mode).*

---

## Safe Device Recovery Guide

If you need to unpin or exit ToddlerOS without using the touchscreen:
```bash
adb shell am force-stop com.toddleros.launcher
```
To unpin via ADB if Device Owner is active:
```bash
adb shell dpm remove-active-admin com.toddleros.launcher/.kiosk.ToddlerDeviceAdminReceiver
```
To uninstall cleanly:
```bash
adb shell pm uninstall com.toddleros.launcher
```

---

## Build & Installation

### Prerequisites
- JDK 17
- Android SDK Platform 35 (minSdk 26, targetSdk 31)
- Android Studio or Gradle 8.7+

### Building the APK
```bash
./gradlew assembleDebug
```
Output:
`app/build/outputs/apk/debug/app-debug.apk`

### Installing via ADB
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```
Press the **Home** button on your device and select **ToddlerOS** as the default Home application.

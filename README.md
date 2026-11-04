# BD Mess - Smart Mess Living & Shared Expense Manager for Android

A specialized Android application engineered for university students, professionals, and bachelor residents in Bangladesh. Built with Kotlin, Jetpack Compose, Material 3, and integrated with the Google Gemini API for automated expense auditing.

---

## Project Overview

Managing shared apartment expenses (commonly known as "bachelor mess" accounting in Bangladesh) involves continuous tracking of daily meals, shared grocery purchases (bazar), house rent, maid allowances, and fluctuating utility bills. 

BD Mess automates this accounting pipeline. It replaces manual paper ledgers with a unified mobile application that records daily meal counts, calculates dynamic meal rates in real time, splits fixed overhead costs, and generates transparent monthly member balance sheets.

---

## Key Features

- Daily Meal Logging: Multi-meal tracking (breakfast, lunch, and dinner) for each registered resident with instant monthly count aggregation.
- Automated Dynamic Meal Rate Engine: Continuously calculates the active mess meal rate using the standard formula:
  `Meal Rate = Total Monthly Grocery Cost / Total Cumulative Meals Consumed`
- Bazar & Grocery Accounting: Records individual grocery contributions, shopping trip receipts, and automatically calculates member balances.
- Fixed Utility & Rent Splitter: Evenly distributes house rent, cook/maid fees, gas cylinders, electricity, water, and internet bills across all active flatmates.
- Individual Member Statements: Generates itemized monthly statements for each resident detailing total meals eaten, meal expense, fixed cost share, advance payments made, and net refundable or payable amounts in Bangladeshi Taka (BDT).
- Native Android System Integration: Engineered with modern Jetpack Compose edge-to-edge display handling (`enableEdgeToEdge`, `statusBarsPadding`, `navigationBarsPadding`), responsive soft-keyboard insets (`imePadding`), and hardware back-button navigation.
- Native JavaScript Bridge: Secure Android-to-Web interface facilitating bidirectional clipboard data exchange, native system toasts, and deep-link handling.
- Intelligent Expense Assistant: Integrated server-side Google Gemini API workflow to summarize monthly expenses and answer resident accounting queries.

---

## Tech Stack

- Platform: Android (minSdk: 24 / Android 7.0, targetSdk: 36)
- Core Language: Kotlin
- UI Framework: Jetpack Compose with Material Design 3
- Architecture: ComponentActivity, Compose Scaffold, WebView Native Bridge
- Build System: Gradle Kotlin DSL (`build.gradle.kts`) with Version Catalogs
- Testing: AndroidX Test Runner, Robolectric, Roborazzi Screenshot Testing
- Cloud & APIs: Google Gemini API, Google Services

---

## Project Structure

```
BD-Mess-App/
├── app/
│   ├── build.gradle.kts       # Android module build configurations and plugins
│   ├── proguard-rules.pro     # Release code shrinking and obfuscation rules
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml   # Application permissions and activities
│       │   ├── java/com/example/
│       │   │   ├── MainActivity.kt   # Compose container and WebView bridge
│       │   │   └── ui/theme/         # Material 3 colors, typography, theme
│       │   ├── assets/               # Progressive Web App assets and manifests
│       │   └── res/                  # Vector drawables, mipmaps, XML values
│       └── test/                     # Unit and screenshot regression tests
├── build.gradle.kts           # Root Gradle build script
├── settings.gradle.kts        # Repository and module declarations
├── manifest.json              # PWA specification and configuration
├── metadata.json              # Application capability declarations
└── .env.example               # Gemini API credential template
```

---

## Installation and Setup Guide

### Prerequisites
- Android Studio Ladybug or newer
- JDK 17 or higher
- Android SDK 36 platform and build tools installed

### 1. Clone the Repository
```bash
git clone https://github.com/labibfohayer/BD-Mess-App.git
cd BD-Mess-App
```

### 2. Configure Environment Variables
Copy `.env.example` to `.env` in the root directory:
```bash
cp .env.example .env
```
Provide your Gemini API key in the `.env` file:
```env
GEMINI_API_KEY=your_gemini_api_key_here
```

### 3. Open Project in Android Studio
1. Launch Android Studio.
2. Select **Open** and select the `BD-Mess-App` directory.
3. Allow Gradle to synchronize dependencies.

### 4. Build and Run on Emulator or Physical Device
- To build the debug APK via CLI:
```bash
./gradlew assembleDebug
```
- Or click the **Run** button (Shift + F10) in Android Studio to install directly on an attached Android device.

---

## Release Build & Keystore Configuration

To compile a signed production release APK / AAB:
1. Ensure your signing keystore path is exported via the `KEYSTORE_PATH` environment variable.
2. Run the Gradle release task:
```bash
./gradlew assembleRelease
```
The resulting APK will be generated under `app/build/outputs/apk/release/`.

---

## Author and Contact

Developed by **Labib Fohayer**.

- GitHub: [https://github.com/labibfohayer](https://github.com/labibfohayer)
- Portfolio: [https://labibfohayer.github.io](https://labibfohayer.github.io)

---

## License

This project is licensed under the Apache 2.0 License.

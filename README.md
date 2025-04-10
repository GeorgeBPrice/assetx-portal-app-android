# AssetX Android App

## Overview

The **AssetX Android App** is a mobile companion to the **AssetX SaaS platform**, a fully cloud-based solution designed to unify **IT Asset Management (ITAM)** and **Technology Expense Management (TEM)**. Built as a personal experiment, this app leverages **Android Studio**, **Kotlin**, **Retrofit**, **MPAndroidChart**, **JUnit**, and **Mockito** to provide an alternative way for AssetX.ai users to access key features of the platform on their mobile devices.

AssetX aims to empower organizations with real-time asset control, AI-driven insights, and Fluid Analytics to optimize IT assets and telecom costs efficiently. With seamless asset lifecycle management, on-demand auditing, AI-assisted integrations, intelligent data management, and rich reporting, AssetX delivers these capabilities within an intuitive, user-friendly interface. This Android app serves as an experimental portal into that ecosystem.

**Note**: This project is a personal experiment and not an official part of the AssetX.ai platform.

![Screenshot_Dashboard](https://github.com/user-attachments/assets/c03e3d55-6181-44dc-bcdf-c678f1c7e0f1)


---

## Features (Stage 1)

The current version (Stage 1) focuses on delivering a functional dashboard to visualize asset and expense data:

- **Dashboard Screen**:
    - Displays an **Asset Overview** and **Expense Overview** with interactive charts.
    - Built using **Compose** for a modern, declarative UI.
    - Includes a top app bar and bottom navigation bar for future extensibility.

- **Asset Charts**:
    - Visualizes asset data with options: "Assets by Type", "Assets by Status", "Assets by Personnel", "Value by Personnel", and "Value by Assets".
    - Uses **MPAndroidChart** for bar chart rendering with custom formatting (e.g., rounding values to thousands, integer counts).
    - Features a reset button (refresh icon) to restore default chart view.

- **Expense Charts**:
    - Visualizes expense data with options: "Expenses by Payment Status", "Expenses by Vendor", "Total Amount by Month", and "Total Amount by Year".
    - Supports horizontal bar charts with vendor name mapping (currently hardcoded, pending API integration).
    - Includes a reset button for chart navigation.

- **Data Integration**:
    - Fetches data from the AssetX API using **Retrofit** (currently via `DashboardViewModel`).
    - Displays real-time asset and expense metrics pulled from the `api/asset/complex` and `api/expense/complex` endpoints.

- **Testing**:
    - Unit tests for chart data transformation using **JUnit** and **Mockito**.
    - Integration tests for API data retrieval and UI rendering with **MockWebServer** and **Compose Test Rule**.

---

## Development Environment

- **IDE**: Android Studio
- **Language**: Kotlin
- **Networking**: Retrofit
- **Charting**: MPAndroidChart
- **Testing**: JUnit, Mockito, MockWebServer, AndroidX Test
- **Minimum OS Version**: Android 8.0 (API 26) - Ensures compatibility with a wide range of devices while supporting modern Compose features.
- **Target OS Version**: Android 14 (API 34) - Optimized for the latest Android features and security.

---

## Development Stages

### Stage 1: Dashboard Visualization (Current)
- **Objective**: Build a functional dashboard to display asset and expense data from the AssetX API.
- **Achievements**:
    - Implemented a responsive UI with Compose and MPAndroidChart.
    - Integrated Retrofit for API calls to fetch asset and expense data.
    - Added basic unit and integration tests for data transformation and API interaction.
- **Status**: Complete, with ongoing refinements (e.g., dynamic vendor mapping).

### Stage 2: Enhanced Features (Planned)
- **Objective**: Expand functionality and interactivity.
- **Features**:
    - Add asset and expense detail screens with CRUD operations (Create, Read, Update, Delete).
    - Implement real-time AI-driven insights (e.g., cost optimization suggestions).
    - Enable on-demand auditing with exportable reports (PDF/CSV).
- **Tech Additions**: Room database for offline caching, WorkManager for background sync.

### Stage 3: Full Integration and Polish (Planned)
- **Objective**: Mirror core AssetX SaaS features on mobile.
- **Features**:
    - Seamless asset lifecycle management with barcode/QR code scanning.
    - Fluid Analytics with customizable dashboards and filters.
    - User authentication and role-based access (e.g., OAuth2).
- **Tech Additions**: CameraX for scanning, Firebase Authentication, Jetpack Navigation.

---

## Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/GeorgeBPrice/assetx-portal-app-android.git
   cd assetx-portal-app-android
   ```

2. **Set Up Android Studio**:
    - Open the project in Android Studio.
    - Ensure the Android SDK (API 26+) is installed.

3. **Configure API**:
    - Update `RetrofitInstance.kt` with your AssetX API base URL and credentials (if applicable).

4. **Build and Run**:
    - Sync Gradle (`File > Sync Project with Gradle Files`).
    - Run the app on an emulator or device (API 26+).

---

## Running Tests

- **Unit Tests**:
  ```bash
  ./gradlew test
  ```
    - Located in `src/test/java/com/example/assetxandroidapp`.

- **Integration Tests**:
  ```bash
  ./gradlew connectedCheck
  ```
    - Located in `src/androidTest/java/com/example/assetxandroidapp`.
    - Requires a connected device or emulator.

---

## Future Considerations

An Android app for a SaaS platform like AssetX could include additional features and considerations:

- **Push Notifications**: Notify users of asset status changes or expense alerts using Firebase Cloud Messaging (FCM).
- **Multi-Language Support**: Add localization for broader accessibility.
- **Accessibility**: Ensure compliance with WCAG standards (e.g., screen reader support).
- **Performance Monitoring**: Integrate tools like Firebase Performance Monitoring or Sentry for crash reporting.
- **Security**: Encrypt sensitive data locally and use HTTPS for API calls (already implemented via Retrofit).
- **Cross-Platform**: Explore Kotlin Multiplatform Mobile (KMM) for an iOS companion app.

---

## Project Structure

```
assetx-android-app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/assetxandroidapp/
│   │   │   │   ├── MainActivity.kt         # Dashboard UI and chart logic
│   │   │   │   ├── data/                  # Data models (Asset, Expense)
│   │   │   │   ├── network/               # Retrofit setup (AssetApi, RetrofitInstance)
│   │   │   │   ├── viewmodel/             # DashboardViewModel for data handling
│   │   │   ├── res/                       # Layouts (e.g., marker_view.xml)
│   │   ├── test/                          # Unit tests
│   │   ├── androidTest/                   # Integration tests
│   ├── build.gradle.kts                   # App dependencies and config
├── gradle/
│   ├── libs.versions.toml                 # Version catalog for dependencies
├── README.md                              # This file
```

---

## Contributing

This is a personal experiment, but feedback and suggestions are welcome! Feel free to fork the repository and submit pull requests with improvements.

---

## License

This project is unlicensed and intended for personal experimentation only. It is not affiliated with the official AssetX.ai platform.

---

### Notes
- **OS Version**: I chose Android 8.0 (API 26) as the minimum to balance compatibility and modern feature support (e.g., Compose). Targeting Android 14 ensures it’s optimized for the latest devices.
- **What Else?**: Added ideas like push notifications and accessibility, which are common in production Android apps but optional for an experiment.
- **Stage 1 Focus**: Kept the README focused on the current dashboard while hinting at future potential.

Let me know if you’d like to adjust the stages, add more details, or refine anything else!

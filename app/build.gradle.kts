import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

// Read the local.properties file
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {
    namespace = "com.ayoo.consumer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ayoo.consumer"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // Securely read the Google Client ID, remove any quotes, and make it available
        val googleClientId =
            (localProperties.getProperty("GOOGLE_WEB_CLIENT_ID") ?: "").replace("\"", "")
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleClientId\"")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2024.05.00")
    implementation(composeBom)

    // Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")

    // Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.foundation:foundation")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.compose.material:material") // Added for pull-to-refresh

    // Jetpack Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Debugging
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // ViewModel for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    // Google Maps for Compose
    implementation("com.google.maps.android:maps-compose:6.12.2")
    implementation("com.google.android.gms:play-services-maps:19.2.0")

    // Backendless
    implementation("com.backendless:backendless:7.0-alpha")

    // Google Auth & Location
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
}

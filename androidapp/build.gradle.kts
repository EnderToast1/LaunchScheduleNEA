plugins {
    alias(libs.plugins.androidApplication)
    kotlin("android")
}

android {
    namespace = "com.niaw.launchnea"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.niaw.launchnea"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.9.3"
    }
}

dependencies {
    implementation(project(":composeApp")) // shared KMP module
    debugImplementation(libs.androidx.compose.ui.tooling)
}

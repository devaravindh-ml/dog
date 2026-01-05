// mylibrary/build.gradle.kts

// Apply the Android Library plugin instead of the application plugin
plugins {
   // Use the library plugin alias
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.mylibrary" // Must be unique
    compileSdk = 36 // Match your app's compileSdk

    defaultConfig {
        minSdk = 24 // Match your app's minSdk
        targetSdk = 36 // Match your app's targetSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    kotlinOptions {
        jvmTarget = "11"
    }

    // Optional: Add build features if this library uses ViewBinding or DataBinding
    buildFeatures {
        // viewBinding = true 
    }
}

dependencies {
    // Add dependencies that the library itself needs, e.g.,
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    
    // Testing dependencies (optional, but good practice)
    testImplementation(libs.junit)
}
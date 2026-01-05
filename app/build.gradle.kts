plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.dog"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.dog"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // CRITICAL FIX: The redundant and incorrectly nested 'android' block is removed.
    // The buildFeatures block is placed correctly inside the main android block.
    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

// FIX: This line is misplaced and unnecessary for dependency definition. It must be removed.
// val implementationConfig = configurations.findByName("implementation")

dependencies {

    // --- Standard Android and Kotlin Libraries (using libs) ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material) // Primary source for Material Components (TabLayout, FAB, etc.)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // --- Version Catalog Dependencies (Data) ---
    // NOTE: These assume you have 'data-utils' defined in libs.versions.toml

    // --- Module Dependency ---


    // --- Third-party Libraries (Explicit Versions) ---
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // For Image Loading (Needed by PetListAdapter.kt)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // --- Testing Dependencies ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // REMOVED REDUNDANT LINES:
    // implementation("com.google.android.material:material:1.12.0") <-- Redundant with libs.material
    // implementation("androidx.core:core-ktx:1.9.0")               <-- Redundant with libs.androidx.core.ktx
    // testImplementation("junit:junit:4.13.2")                      <-- Redundant with libs.junit
}
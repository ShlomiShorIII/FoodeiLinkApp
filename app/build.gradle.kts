plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.example.foodielink"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.foodielink"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.firestore) // For small data, like text
    implementation(libs.firebase.storage) // For big data, like pictures
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.activity:activity:1.10.1")
    implementation("com.google.firebase:firebase-auth:23.2.1")
    implementation("androidx.core:core:1.16.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.google.android.gms:play-services-maps:19.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("io.github.ParkSangGwon:tedimagepicker:1.6.1") // TedImagePicker from GitHub
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("com.github.Yalantis:Koloda-Android:v0.0.2-alpha") // SwipeCards from GitHub
}

apply(plugin = "com.google.gms.google-services")
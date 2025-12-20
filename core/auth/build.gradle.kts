import Dependencies.Plugins.ksp

plugins {
    id(Dependencies.Plugins.androidLibrary)
    id(Dependencies.Plugins.kotlinAndroid)
    id(Dependencies.Plugins.ksp)
    id(Dependencies.Plugins.hilt)
}

android {
    namespace = "com.example.core.auth"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:data"))

    implementation(Dependencies.Hilt.android)
    implementation(Dependencies.Network.kotlinxSerialization)
    implementation(Dependencies.Network.okhttp)
    implementation(Dependencies.Network.retrofit)
    implementation(Dependencies.Network.okhttpLogging)
    ksp(Dependencies.Hilt.compiler)

    implementation(Dependencies.Coroutines.android)
    implementation(Dependencies.Coroutines.core)
}
plugins {
    id(Dependencies.Plugins.androidLibrary)
    id(Dependencies.Plugins.kotlinAndroid)
    id(Dependencies.Plugins.ksp)
    id(Dependencies.Plugins.hilt)
    id(Dependencies.Plugins.kotlinSerialization)
}

android {
    namespace = "com.example.moneyswift.network"
    compileSdk = AndroidConfig.compileSdk

    defaultConfig {
        minSdk = AndroidConfig.minSdk
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

    implementation(Dependencies.Network.kotlinxSerialization)
    implementation(Dependencies.Network.retrofitSerialization)

    implementation(Dependencies.Hilt.android)
    ksp(Dependencies.Hilt.compiler)

    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.Coroutines.android)

    implementation(platform(Dependencies.Firebase.bom))
    implementation(Dependencies.Firebase.auth)

    implementation(Dependencies.Network.okhttp)
    implementation(Dependencies.Network.okhttpLogging)
    implementation(Dependencies.Network.retrofit)

}

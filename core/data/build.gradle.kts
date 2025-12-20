plugins {
    id(Dependencies.Plugins.androidLibrary)
    id(Dependencies.Plugins.kotlinAndroid)
    id(Dependencies.Plugins.ksp)
}

android {
    namespace = "com.example.moneyswift.data"
    compileSdk = AndroidConfig.compileSdk

    defaultConfig {
        minSdk = AndroidConfig.minSdk
        testInstrumentationRunner = AndroidConfig.testInstrumentationRunner
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
    implementation(project(":core:network"))
    implementation(project(":core:database"))

    implementation(Dependencies.Hilt.android)
    ksp(Dependencies.Hilt.compiler)

    implementation(Dependencies.Network.retrofitSerialization)
    implementation(Dependencies.Network.kotlinxSerialization)

    implementation(Dependencies.Stripe.android)
    implementation(Dependencies.Stripe.financialConnections)

    implementation(platform(Dependencies.Firebase.bom))
    implementation(Dependencies.Firebase.auth)

    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.Coroutines.android)
}

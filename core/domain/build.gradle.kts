plugins {
    id(Dependencies.Plugins.androidLibrary)
    id(Dependencies.Plugins.kotlinAndroid)
    id(Dependencies.Plugins.ksp)
    id(Dependencies.Plugins.kotlinSerialization)
}

android {
    namespace = "com.example.moneyswift.domain"
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
    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.Network.retrofitSerialization)
    implementation(Dependencies.Network.kotlinxSerialization)

    implementation(platform(Dependencies.Firebase.bom))
    implementation(Dependencies.Firebase.database)

    implementation(Dependencies.Stripe.android)
    implementation(Dependencies.Stripe.financialConnections)

    implementation(Dependencies.Hilt.android)
    implementation(Dependencies.Hilt.compiler)
    ksp(Dependencies.Hilt.compiler)

    testImplementation(Dependencies.Test.junit)
}
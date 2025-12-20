plugins {
    id(Dependencies.Plugins.androidLibrary)
    id(Dependencies.Plugins.kotlinAndroid)
    id(Dependencies.Plugins.ksp)
}

android {
    namespace = "com.example.core.navigation"
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

    implementation(Dependencies.Hilt.android)
    implementation(Dependencies.Hilt.compiler)
    implementation(Dependencies.Hilt.navigationCompose)
    implementation(Dependencies.Coroutines.android)
    implementation(Dependencies.Coroutines.core)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiGraphics)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.uiToolingPreview)
    implementation(Dependencies.Compose.material3)
    implementation(Dependencies.Compose.materialIconsExtended)
    implementation(Dependencies.Compose.runtime)


    ksp(Dependencies.Hilt.compiler)

    implementation(Dependencies.Nav3.ui)
    implementation(Dependencies.Nav3.runtime)
    implementation(Dependencies.Nav3.adaptive)
    implementation(Dependencies.Nav3.lifecycleViewModel)
    implementation(Dependencies.AndroidX.coreKtx)

    testImplementation(Dependencies.Test.junit)
}
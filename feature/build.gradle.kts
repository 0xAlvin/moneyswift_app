import java.util.Properties

plugins {
    id(Dependencies.Plugins.androidLibrary)
    id(Dependencies.Plugins.kotlinAndroid)
    id(Dependencies.Plugins.ksp)
    id(Dependencies.Plugins.kotlinCompose)
}

android {
    namespace = "com.example.feature"
    compileSdk = 36

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }


    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }

    defaultConfig {
        minSdk = 24


        buildConfigField(
            "String",
            "STRIPE_PUBLISHABLE_KEY",
            "\"${localProperties.getProperty("stripe.publishable.key", "")}\""
        )

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
    implementation(project(":core:navigation"))
    implementation(project(":core:network"))
    implementation(project(":ui"))


    implementation(Dependencies.Hilt.android)
    implementation(Dependencies.Hilt.navigationCompose)
    ksp(Dependencies.Hilt.compiler)


    implementation(Dependencies.Stripe.android)
    implementation(Dependencies.Stripe.financialConnections)


    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.compose.ui:ui-viewbinding:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.13.0")


    implementation(Dependencies.Coroutines.android)
    implementation(Dependencies.Coroutines.core)


    implementation(Dependencies.Network.kotlinxSerialization)


    implementation(Dependencies.Coil.compose)
    implementation(Dependencies.Coil.network)


    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiGraphics)
    implementation(Dependencies.Compose.runtime)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.uiToolingPreview)
    implementation(Dependencies.Compose.material3)
    implementation(Dependencies.Compose.materialIconsExtended)
    debugImplementation(Dependencies.Compose.uiTooling)


    api(Dependencies.Nav3.runtime)
    implementation(Dependencies.Nav3.adaptive)


    implementation(platform(Dependencies.Firebase.bom))
    implementation(Dependencies.Firebase.auth)
}
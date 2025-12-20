import java.util.Properties

plugins {
    id(Dependencies.Plugins.androidApplication)
    id(Dependencies.Plugins.kotlinAndroid)
    id(Dependencies.Plugins.kotlinCompose)
    id(Dependencies.Plugins.hilt)
    id(Dependencies.Plugins.ksp)
    id(Dependencies.Plugins.googleServices)
}

android {
    namespace = "com.example.moneyswift"
    compileSdk = AndroidConfig.compileSdk

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }

    defaultConfig {
        applicationId = "com.example.moneyswift"
        minSdk = AndroidConfig.minSdk
        targetSdk = AndroidConfig.targetSdk
        versionCode = AndroidConfig.versionCode
        versionName = AndroidConfig.versionName

        testInstrumentationRunner = AndroidConfig.testInstrumentationRunner

        buildConfigField(
            "String",
            "STRIPE_PUBLISHABLE_KEY",
            "\"${localProperties.getProperty("stripe.publishable.key", "pk_test_51Sbt3V9QaYTwcxUIaOZitN0YWzmi5UHimWOIr8bVQlbsqMfgKRiDfYwSLasw0Q4y7At3GqpOhzp2o8Ti3LwzJmlL00a6nyNoK1")}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources.excludes.add("META-INF/gradle/incremental.annotation.processors")
    }
}

dependencies {
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.activityCompose)
    implementation(Dependencies.AndroidX.Lifecycle.runtime)
    implementation(Dependencies.AndroidX.Lifecycle.viewModel)

    implementation(Dependencies.Hilt.android)
    implementation(Dependencies.Hilt.navigationCompose)
    ksp(Dependencies.Hilt.compiler)

    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiGraphics)
    implementation(Dependencies.Compose.uiToolingPreview)
    implementation(Dependencies.Compose.material3)
    debugImplementation(Dependencies.Compose.uiTooling)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.splashScreen)

    implementation(Dependencies.Stripe.android)
    implementation(Dependencies.Stripe.financialConnections)

    implementation(Dependencies.Nav3.runtime)
    implementation(Dependencies.Nav3.ui)
    implementation(Dependencies.Nav3.lifecycleViewModel)

    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:auth"))
    implementation(project(":core:data"))
    implementation(project(":ui"))
    implementation(project(":feature"))
    implementation(project(":core:navigation"))

    implementation(platform(Dependencies.Firebase.bom))
    implementation(Dependencies.Firebase.auth)

    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.Test.junitExt)
}
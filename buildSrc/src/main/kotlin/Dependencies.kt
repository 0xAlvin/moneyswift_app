object Dependencies {

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:${Versions.androidxCore}"
        const val activityCompose = "androidx.activity:activity-compose:${Versions.androidxActivity}"



        object Lifecycle {
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidxLifecycle}"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidxLifecycle}"
            const val runtimeCompose = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.androidxLifecycle}"
        }
    }
    object Plugins {
        const val androidApplication = "com.android.application"
        const val androidLibrary = "com.android.library"
        const val kotlinAndroid = "org.jetbrains.kotlin.android"
        const val kotlinCompose = "org.jetbrains.kotlin.plugin.compose"
        const val hilt = "com.google.dagger.hilt.android"
        const val ksp = "com.google.devtools.ksp"
        const val googleServices = "com.google.gms.google-services"
        const val kotlinSerialization = "org.jetbrains.kotlin.plugin.serialization"
    }

    object Compose {
        const val ui = "androidx.compose.ui:ui:${Versions.compose}"

        const val splashScreen = "androidx.core:core-splashscreen:${Versions.splash}"
        const val uiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
        const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
        const val material3 = "androidx.compose.material3:material3:${Versions.composeMaterial3}"
        const val foundation = "androidx.compose.foundation:foundation:${Versions.compose}"
        const val materialIconsExtended = "androidx.compose.material:material-icons-extended:${Versions.composeMaterialIconsExtended}"
        const val runtime = "androidx.compose.runtime:runtime:${Versions.compose}"
    }

    object Hilt {
        const val android = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
        const val navigationCompose = "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationCompose}"
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:${Versions.room}"
        const val ktx = "androidx.room:room-ktx:${Versions.room}"
        const val compiler = "androidx.room:room-compiler:${Versions.room}"
    }

    object Network {
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
        const val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
        const val kotlinxSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerialization}"
        const val retrofitSerialization = "com.squareup.retrofit2:converter-kotlinx-serialization:${Versions.retrofitKotlinxSerialization}"
    }

    object Nav3 {
        const val runtime = "androidx.navigation3:navigation3-runtime:${Versions.navigation3}"
        const val ui = "androidx.navigation3:navigation3-ui:${Versions.navigation3}"
        const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-navigation3:${Versions.androidxLifecycleNav3}"
        const val adaptive = "androidx.compose.material3.adaptive:adaptive-navigation3:${Versions.material3AdaptiveNav3}"
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
        const val auth = "com.google.firebase:firebase-auth"
        const val database = "com.google.firebase:firebase-database"
        const val fcm = "com.google.firebase:firebase-messaging"
    }

    object Coroutines {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    }

    object Coil {
            const val compose = "io.coil-kt.coil3:coil-compose:${Versions.coil}"
            const val network = "io.coil-kt.coil3:coil-network-okhttp:${Versions.coil}"
    }
    object Stripe {
        const val android = "com.stripe:stripe-android:${Versions.stripe}"
        const val financialConnections = "com.stripe:financial-connections:${Versions.stripeFinancialConnections}"
    }

    object Test {
        const val junit = "junit:junit:${Versions.junit}"
        const val junitExt = "androidx.test.ext:junit:${Versions.junitExt}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
        const val composeUi = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"
        const val composeUiManifest = "androidx.compose.ui:ui-test-manifest:${Versions.compose}"
    }
}

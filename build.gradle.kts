
import java.util.Properties

plugins {
    id(Dependencies.Plugins.androidApplication) version Versions.agp apply false
    id(Dependencies.Plugins.androidLibrary) version Versions.agp apply false
    id(Dependencies.Plugins.kotlinAndroid) version Versions.kotlin apply false
    id(Dependencies.Plugins.kotlinCompose) version Versions.kotlin apply false
    id(Dependencies.Plugins.hilt) version Versions.hilt apply false
    id(Dependencies.Plugins.ksp) version Versions.ksp apply false
    id(Dependencies.Plugins.googleServices) version Versions.googleServices apply false
    id(Dependencies.Plugins.kotlinSerialization) version Versions.kotlin apply false
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

extra["stripePublishableKey"] = localProperties.getProperty("stripe.publishable.key", "")

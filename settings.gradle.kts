pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}

rootProject.name = "MoneySwift"
include(":app")
include(":core:domain")
include(":core:network")
include(":core:database")
include(":core:data")
include(":core:auth")
include(":ui")
include(":feature")
include(":core:navigation")

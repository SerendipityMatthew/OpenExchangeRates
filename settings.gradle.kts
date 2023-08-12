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
    }
}

rootProject.name = "Open Exchange Rates"
include(":app")
include(":common:network")
include(":common:utils")
include(":common:ui-component")
include(":currency-convert")
include(":common:core-data")

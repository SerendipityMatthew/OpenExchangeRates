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
include(":Network")
include(":BaseUtils")
include(":UIComponent")
include(":CurrencyConvert")
include(":CoreData")

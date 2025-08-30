pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "HappyHabit"
include(":app")
include(":feature:home")
include(":feature:settings")
include(":core:ui")
include(":domain")
include(":data")
include(":core:di")
include(":core:navigation")
include(":feature:detail")
include(":feature:habitdialog")
include(":feature:deleteconfirmationdialog")
include(":feature:weekly")
include(":feature:notification")

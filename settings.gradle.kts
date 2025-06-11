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
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SpaceX"
include(":app")
include(":core:design")
include(":core:data:data-launches")
include(":core:domain:models")
include(":core:network")
include(":core:domain:repository")
include(":feature:launches")
include(":core:ui-common")
include(":feature:launchDetails")
include(":core:testing")
include(":core:utils")
include(":core:data:data-common")

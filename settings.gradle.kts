pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = uri("https://maven.aliyun.com/repository/central"))
        maven(url = uri("https://maven.aliyun.com/repository/google"))
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://maven.aliyun.com/repository/central"))
        maven(url = uri("https://maven.aliyun.com/repository/google"))
    }
}

rootProject.name = "AppUpdater"
include(":app", ":app-updater", ":app-dialog")

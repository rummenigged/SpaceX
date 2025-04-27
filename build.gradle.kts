// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.googleService) apply false
    alias(libs.plugins.google.firebase.appdistribution) apply false
    alias(libs.plugins.firebase.crashlyticsGradlePlugin) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.android.library) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.androidx.navigation.safeArgsGradlePlugin)
        classpath(libs.ktlint.gradlePlugin)
    }

    extra.apply {
        set("applicationId", "com.octopus.edu.kotlin.spacex")
        set("compileSdkVersion", 35)
        set("targetSdkVersion", 35)
        set("minSdkVersion", 28)

        set("sourceCompatibility", JavaVersion.VERSION_11)
        set("targetCompatibility", JavaVersion.VERSION_11)
        set("kotlinOptionsJVMTarget", "11")
    }
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        verbose = true
        android = true
    }
}

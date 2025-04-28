plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.googleService)
    alias(libs.plugins.google.firebase.appdistribution)
    alias(libs.plugins.firebase.crashlyticsGradlePlugin)
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
}

android {
    namespace = rootProject.ext["applicationId"].toString()
    compileSdk = rootProject.ext["compileSdkVersion"].toString().toInt()

    defaultConfig {
        applicationId = rootProject.ext["applicationId"].toString()
        minSdk = rootProject.ext["minSdkVersion"].toString().toInt()
        targetSdk = rootProject.ext["targetSdkVersion"].toString().toInt()
        versionCode = 2
        versionName = "0.2.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }

        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = rootProject.ext["sourceCompatibility"] as JavaVersion
        targetCompatibility = rootProject.ext["targetCompatibility"] as JavaVersion
    }

    kotlinOptions {
        jvmTarget = rootProject.ext["kotlinOptionsJVMTarget"].toString()
    }

    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }

    lint {
        warningsAsErrors = true
        checkDependencies = true
        disable += "AndroidGradlePluginVersion"
        disable += "GradleDependency"
    }
}

tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask> {
    workerMaxHeapSize.set("512m")
}

dependencies {

//    Modules
    implementation(project(":core:design"))
    implementation(project(":core:domain:models"))
    implementation(project(":core:domain:repository"))
    implementation(project(":core:data:data-launches"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.material)
    implementation(libs.androidx.navigation.fragment.ktx)

//    Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

//    Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

//    Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlyticsKtx)
    implementation(libs.firebase.analyticsKtx)

    testImplementation(libs.test.konsist)
    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.androidx.junit)
    androidTestImplementation(libs.test.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.test.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.test.androidx.ui.test.manifest)
}

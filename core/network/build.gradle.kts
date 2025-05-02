plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.adgem.cosmicrewards.core.network"
    compileSdk = rootProject.ext["compileSdkVersion"].toString().toInt()

    defaultConfig {
        minSdk = rootProject.ext["minSdkVersion"].toString().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "SERVER_ROUTE", "\"https://api.spacexdata.com/v3/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        buildConfig = true
    }
}

dependencies {

    implementation(project(":core:domain:models"))

    implementation(libs.androidx.core.ktx)

//    Network
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.converter)
    implementation(libs.moshi.kotlinCodeGen)
    implementation(libs.retrofit)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

//    Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.adgem.cosmicrewards.core.data.launches"
    compileSdk = rootProject.ext["compileSdkVersion"].toString().toInt()

    defaultConfig {
        minSdk = rootProject.ext["minSdkVersion"].toString().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
}

dependencies {

    implementation(project(":core:domain:models"))
    implementation(project(":core:domain:repository"))
    implementation(project(":core:data:data-common"))
    implementation(project(":core:network"))
    implementation(project(":core:utils"))

    implementation(libs.androidx.core.ktx)

//    Network
    implementation(libs.retrofit)
    implementation(libs.moshi.kotlin)

//    Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(project(":core:testing"))
}

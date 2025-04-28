plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = rootProject.ext["applicationId"].toString()
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

    implementation(libs.androidx.core.ktx)

//    Test
    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.androidx.junit)
    androidTestImplementation(libs.test.androidx.espresso.core)
}

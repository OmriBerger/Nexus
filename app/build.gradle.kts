plugins {
    alias(libs.plugins.android.application)
}


android {
    namespace = "io.github.omriberger"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.omriberger"
        minSdk = 35
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


    }

    buildTypes {
        release {
        }
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a")
            isUniversalApk = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // UI & Navigation
    implementation(libs.appcompat)
    implementation(libs.material) // keep only one material library
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Networking & JSON
    implementation(libs.okhttp)
    implementation(libs.gson)

    // Background tasks & splashscreen
    implementation(libs.work.runtime)
    implementation(libs.core.splashscreen)

    // Misc
    implementation(libs.overscroll.decor.android)

    // Compile-time only
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

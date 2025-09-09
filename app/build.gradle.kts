plugins {
    alias(libs.plugins.android.application)
}

// Read the token from Gradle properties (local only, safe from GitHub)
val apiToken: String = project.findProperty("api.token") as String
val apiId: String = project.findProperty("api.id") as String

android {
    android.buildFeatures.buildConfig = true
    namespace = "io.github.omriberger"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.omriberger"
        minSdk = 35
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Inject the token into BuildConfig
        buildConfigField("String", "API_TOKEN", "\"$apiToken\"")
        buildConfigField("String", "API_ID", "\"$apiId\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.google.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.okhttp)
    implementation(libs.gson)
    implementation(libs.work.runtime)
    implementation(libs.core.splashscreen)
    implementation(libs.overscroll.decor.android)

}

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
}

android {
    namespace = "com.sugarygary.instory"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sugarygary.instory"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")
        testInstrumentationRunner = "com.sugarygary.instory.CustomTestRunner"
        renderscriptTargetApi = 26
        renderscriptSupportModeEnabled = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    //Splash Screen API
    implementation(libs.androidx.core.splashscreen)
    //Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.espresso.idling.resource)
    ksp(libs.androidx.room.compiler)
    //Glide
    implementation(libs.glide)
    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)
    //Dagger Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    //KTX
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    //Datastore
    implementation(libs.androidx.datastore.preferences)
    //Coroutine
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    //Swipe Refresh Layout
    implementation(libs.androidx.swiperefreshlayout)
    //Read more text
    implementation(libs.readmore.textview)
    //GPlay Location Service
    implementation(libs.play.services.location)
    //Paging
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.room.paging)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.navigation.testing)
    kspAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    debugImplementation(libs.androidx.fragment.testing)
}
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("kotlin-kapt")
    id ("kotlin-parcelize")
}


android {
    namespace = "com.example.draw_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.draw_android"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(files("libs/sun.misc.BASE64Decoder.jar"))
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation ("androidx.media:media:1.7.0")
    implementation("androidx.compose.material3:material3-android:1.2.0-alpha10")
    //implementation(project(":clip_img"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("com.google.code.gson:gson:2.10.1")

    //Compose UI库
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.compose.ui:ui:1.5.4")
    // Tooling supp("rt (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.5.4")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:1.5.4")
    // Material Design
    implementation("androidx.compose.material:material:1.5.4")
    // Material design icons
    implementation("androidx.compose.material:material-icons-core:1.5.4")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    // Integration with observables
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")
    implementation("androidx.compose.runtime:runtime-rxjava2:1.5.4")
    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")  // 使用适当的版本号
}
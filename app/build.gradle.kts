plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    defaultConfig {
        applicationId = "com.example.draw_android"
        compileSdk = 33
        buildToolsVersion = "33"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    android.viewBinding.isEnabled = true
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.1"
        //kotlinCompilerVersion = "1.5.10" 默认即可，有提示不舒服
        useLiveLiterals = true
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
    implementation(files("libs/sun.misc.BASE64Decoder.jar"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.core:core-ktx:1.9.0")

    //Compose UI库
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.compose.ui:ui:1.3.3")
    // Tooling supp("rt (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.3.3")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:1.3.1")
    // Material Design
    implementation("androidx.compose.material:material:1.3.1")
    // Material design icons
    implementation("androidx.compose.material:material-icons-core:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:1.3.1")
    // Integration with observables
    implementation("androidx.compose.runtime:runtime-livedata:1.3.3")
    implementation("androidx.compose.runtime:runtime-rxjava2:1.3.3")
    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.3")

}
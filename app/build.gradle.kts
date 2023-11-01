plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
}

android {
    val composeVersion: String by rootProject.extra

    defaultConfig {
        applicationId = "com.example.draw_android"
        compileSdk = 34
        buildToolsVersion = "34"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
                //arguments += ["room.schemaLocation"("$projectDir/schemas".toString())]
            }
        }
    }

    buildTypes {
        release {
            @Suppress("UnstableApiUsage")
            isMinifyEnabled = true
            proguardFiles(
                @Suppress("UnstableApiUsage")
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    android.viewBinding.isEnabled = true
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }
    @Suppress("UnstableApiUsage")
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(files("libs/sun.misc.BASE64Decoder.jar"))
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-android:1.2.0-alpha10")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.core:core-ktx:1.10.1")

    //Compose UI库
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:1.5.3")
    // Tooling supp("rt (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.5.3")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:1.5.3")
    // Material Design
    implementation("androidx.compose.material:material:1.5.3")
    // Material design icons
    implementation("androidx.compose.material:material-icons-core:1.5.3")
    implementation("androidx.compose.material:material-icons-extended:1.5.3")
    // Integration with observables
    implementation("androidx.compose.runtime:runtime-livedata:1.5.3")
    implementation("androidx.compose.runtime:runtime-rxjava2:1.5.3")
    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.3")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}
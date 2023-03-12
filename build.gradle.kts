buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}


subprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
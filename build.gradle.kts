buildscript {
    mapOf(
        Pair("minSdkVer", 22),
        Pair("targetSdkVer", 25),
        Pair("compiledSdkVer", 25),
        Pair("buildToolsVer", "26-rc4"),
        Pair("gradleVersion", "7.4.2"),
        Pair("composeVersion", "1.4.3")
    ).entries.forEach {
        project.extra.set(it.key, it.value)
    }
    apply("config.gradle.kts")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        val ext = project.extra
        classpath("com.android.tools.build:gradle:${ext["gradleVersion"]}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")

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
}
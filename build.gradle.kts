// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {

        google()
        mavenCentral()
        val hilt = "2.45"
        dependencies {
            classpath   ("com.google.dagger:hilt-android-gradle-plugin:$hilt")
        }

    }

   /* dependencies {
        classpath ("com.google.gms:google-services:4.3.15")
    }*/
}

plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}
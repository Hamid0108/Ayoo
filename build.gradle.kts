// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.13.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0-RC2" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0-RC2" apply false
    id("org.jetbrains.kotlin.kapt") version "2.2.0-RC2" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

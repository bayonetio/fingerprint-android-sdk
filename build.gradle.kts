// import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
}

buildscript {
    plugins {
        // Support convention plugins written in Kotlin. Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
        // `kotlin-dsl`
        // kotlin("plugin.serialization") version "1.7.10"
    }

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        // classpath("org.jetbrains.kotlinx:kotlinx-gradle-serialization-plugin:1.4.0")
    }
}

// apply(plugin="kotlinx-serialization")

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

/*
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
*/
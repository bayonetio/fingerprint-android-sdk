apply(plugin="kotlin")
apply(plugin="kotlinx-serialization")

plugins {
    id("java-library")

    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
}

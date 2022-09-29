plugins {
    id("org.jetbrains.kotlin.jvm")

    application
}

repositories {
    // mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation(project(":lib"))
}

application {
    // Define the main class for the application.
    mainClass.set("io.bayonet.fingerprint.apps.cmd.AppKt")
}

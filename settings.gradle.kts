pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Add fingerprintjs dependency
        maven(url="https://maven.fpregistry.io/releases")
    }
}
rootProject.name = "Fingerprint Mobile"
include(":app")
include(":lib")

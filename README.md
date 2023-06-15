# fingerprint-android-sdk
The *fingerprint-android-sdk* is a library for *Android* that determines the device's fingerprint and store it in our backend.

This was developed with *Kotlin*.

## Integrate into your project

### Add repository
We decided distribute this with [jitpack](https://jitpack.io). You have to add the repository that add the posibility to download dependencies from *github*.
Add the *jitpack* repository to the *repositories* section in your *gradle* configuration file:
```groovy
dependencyResolutionManagement {
    ...
    repositories {
        ...
        maven {url 'https://jitpack.io'}
        ...
    }
}
```

### Add the dependency
Now, you have to add the dependency to your module with the following line:
```groovy
dependencies {
    implementation 'com.github.bayonetio:fingerprint-android-sdk:VERSION'
}
```

### Example of the use in your app
This is an example of the use of *Fingerprint Android SDK*.
```kotlin
import io.bayonet.fingerprint.core.domain.Token
import io.bayonet.fingerprint.services.FingerprintService

...

val fingerprintService = FingerprintService(
    this,
    "12345678"
)

// Generate the device token
val token: Token = fingerprintService.analyze()
```

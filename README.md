# Fingerprint Android SDK
A basic library to generate the fingerprint of a device.

This is developed with **Kotlin**.

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
package io.bayonet.fingerprint.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import android.widget.TextView

import io.bayonet.fingerprint.core.domain.Token
import io.bayonet.fingerprint.android.services.FingerprintService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val helloTextView = findViewById<TextView>(R.id.hello_text_view)

        runOnUiThread(Runnable {
            val apiKey = "your-fingeprint-api-key"
            val fingerprintService = FingerprintService(
                apiKey,
                this, // The main context
            )

            GlobalScope.launch {
                try {
                    // Generate the device token
                    val token: Token = fingerprintService.analyze()

                    // Show the token generated in the view
                    helloTextView.text = token.bayonetID
                } catch (e: Exception) {
                    println("ERROR ${e}")
                }
            }
        })
    }
}
```
